package com.STR.Module;

import com.STR.entity.*;
import com.STR.mapper.*;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

// 负责每日0点自动进行本日的任务规划，也可进行直接调用来进行当场初始化
@Component
public class DailyTaskManager {
    private final SiteMapper siteMapper;
    private final TaskMapper taskMapper;
    private final TaskInstanceMapper taskInstanceMapper;
    private final TaskSiteInstanceMapper taskSiteInstanceMapper;
    private final NormalInspectionMapper normalInspectionMapper;
    private final StringRedisTemplate redisTemplate;

    public DailyTaskManager(SiteMapper siteMapper, TaskMapper taskMapper, TaskInstanceMapper taskInstanceMapper, TaskSiteInstanceMapper taskSiteInstanceMapper, NormalInspectionMapper normalInspectionMapper, StringRedisTemplate redisTemplate) {
        this.siteMapper = siteMapper;
        this.taskMapper = taskMapper;
        this.taskInstanceMapper = taskInstanceMapper;
        this.taskSiteInstanceMapper = taskSiteInstanceMapper;
        this.normalInspectionMapper = normalInspectionMapper;
        this.redisTemplate = redisTemplate;
    }

    // 每日进行全局任务下发
    @Scheduled(cron = "0 10 0 * * ?")
    public void initTodayTask(){
        // 处理所有昨日任务
        checkYesterdayTask();
        // 找到所有任务
        List<Task> tasks = taskMapper.selectByCondition(new HashMap<>());
        executeTask(tasks);
    }

    // 根据传入Task的点位池生成对应的本日TaskInstance与TaskSiteInstance 并将关键信息传入Redis缓存
    // 但是 这里并不负责NextCheckTime的计算 相关计算有单独的函数 并在每一次完成TaskSiteInstance时被调取
    public void executeTask(List<Task> tasks) {
        for(Task task : tasks){
            // 如果任务未激活 那就算了
            if(task.getState() == 0) continue;
            // 获取该任务点位池 从中筛选出今日任务地点todaySites
            Map<String,Object> map = new HashMap<>();
            map.put("task_id",task.getTask_id());
            List<Site> sitePool = siteMapper.selectByCondition(map);

            System.out.println("SitePool:" + sitePool.toString());

            List<Site> todaySites = new ArrayList<Site>();
            for (Site site : sitePool){
                if(Objects.equals(site.getUsability(), "不可用")) continue;
                if (site.getNext_check_date() != null && site.getNext_check_date().isEqual(LocalDate.now())){
                    todaySites.add(site);
                } else if(site.getLast_check_time() == null){
                    site.setNext_check_date(LocalDate.now());
                    todaySites.add(site);
                    siteMapper.updateTimes(site);
                } else {
                    if (site.getNext_check_date().isBefore(LocalDate.now())) {
                        site.setNext_check_date(LocalDate.now());
                        todaySites.add(site);
                        siteMapper.updateTimes(site);
                    }
                }
            }
            System.out.println("TodaySites:" + todaySites.toString());
            // 如果没有点位符合任务要求，那就算了
            if(todaySites.isEmpty()) return;

            // 根据TodaySites中的每个点位的巡检日巡检次数 组装本日的 1-N 个SiteList -> sitesListCollection 和 TaskInstance
            List<ArrayList<Site>> sitesListCollection = new ArrayList<>();
            ArrayList<Site> siteList = null;
            while(true){
                siteList = new ArrayList<>();
                for(Site site : todaySites){
                    System.out.println("当前点位为:" + site.toString());
                    List<Integer> frequency = site.getFrequency();
                    //一个点位一天可能要求巡检多次 如果还有巡检次数 那么就加入当前的SitesList
                    if(frequency.get(2) > 0){
                        siteList.add(site);
                        frequency.set(2,frequency.get(2) - 1);
                        site.setFrequency(frequency);
                    }
                }
                // 当todaySites中每个点位的频率都被耗尽 再也无法产出新的点位列表时 就停止
                if (siteList.isEmpty()) break;
                System.out.println("本次SiteList是:"+ siteList.toString());
                sitesListCollection.add(siteList);
            }

            // 针对每个sitesListCollection 中的每个 siteList 组装 TaskInstance TaskSiteInstance
            for(List<Site> oneSiteList : sitesListCollection){
                // 【调用蚁群优化算法 将oneSiteList中的点位进行排序处理 生成新的排好序的oneSiteList】


                // 组装TaskInstance 存入数据库 并获得TaskInstance_id 作为生成TaskSiteInstance的凭据
                TaskInstance taskInstance = TaskInstance.builder()
                        .task_id(task.getTask_id())
                        .start_time(LocalDateTime.now())
                        .user_id(task.getUser_id())
                        .state(0)
                        .task_name(task.getName())
                        .task_description(task.getDescription())
                        .build();
                taskInstanceMapper.insertTaskInstance(taskInstance);

                // 将排好序的 oneSiteList 中的每个点位按顺序依次生成 TaskSiteInstance 存入数据库
                // 对应的 NormalInspection 会在巡检完成时被直接插入数据库
                // 【以TodayTasks为总KEY: 将以TaskInstanceID为KEY TaskSiteInstanceSiteID为内容的 List 存入Redis缓存层 随着今日的巡检员的活动进行更新】
                // 【每完成一个点位的巡检 将一个TaskSiteInstanceSiteID从对应TaskInstanceID的List中去除】
                redisTemplate.opsForSet().add("TodayTasks", String.valueOf(taskInstance.getTaskinstance_id()));
                for(Site site: oneSiteList){
                    TaskSiteInstance taskSiteInstance = TaskSiteInstance.builder()
                            .site_id(site.getSite_id())
                            .taskinstance_id(taskInstance.getTaskinstance_id())
                            .state(0)
                            .user_id(task.getUser_id())
                            .build();
                    taskSiteInstanceMapper.insert(taskSiteInstance);
                    redisTemplate.opsForSet().add(String.valueOf(taskInstance.getTaskinstance_id()), String.valueOf(taskSiteInstance.getTasksiteinstance_id()));
                }
                System.out.println("======================================================");
                System.out.println("今天的任务实例有:");
                System.out.println(redisTemplate.opsForSet().members("TodayTasks"));
                System.out.println("此任务实例点位ID是:");
                System.out.println(redisTemplate.opsForSet().members(String.valueOf(taskInstance.getTaskinstance_id())));
                System.out.println("======================================================");
            }
        }
    }

    // 对于指定的Task的今日任务进行撤销 可能是误激活 或当事人临时有事无法巡检
    public void undoTask(Task task){
        Map<String,Object> map = new HashMap<>();
        map.put("task_id", task.getTask_id());
        map.put("timestamp",LocalDate.now());
        List<TaskInstance> taskInstanceList = taskInstanceMapper.findByCondition(map);
        for(TaskInstance taskInstance : taskInstanceList){

            // 删除所有TaskSiteInstance 下属的 NormalInspection 当然 这意味着巡检到一半忽然撤销任务了 或者本来巡检完成了 撤销了
            Map<String,Object> map1 = new HashMap<>();
            map1.put("taskinstance_id",taskInstance.getTaskinstance_id());
            List<TaskSiteInstance> taskSiteInstances = taskSiteInstanceMapper.findByCondition(map1);
            for(TaskSiteInstance taskSiteInstance : taskSiteInstances){
                normalInspectionMapper.deleteByTaskSiteInstanceID(taskSiteInstance.getTasksiteinstance_id());
            }

            // 删除所有对应的TaskSiteInstance
            taskSiteInstanceMapper.deleteByTaskInstanceID(taskInstance.getTaskinstance_id());
            // 清除该TaskInstance在Redis中的缓存信息 如果有的话
            redisTemplate.opsForSet().remove("TodayTasks",String.valueOf(taskInstance.getTaskinstance_id()));
            redisTemplate.delete(String.valueOf(taskInstance.getTaskinstance_id()));
            // 删除该TaskInstance的数据库记录
            taskInstanceMapper.deleteByID(taskInstance.getTaskinstance_id());
        }
    }

    // 对前一日的未完成任务进行审查 查找Redis中TodayTasks为KEY的List的遗留数据 对TaskInstance进行已超时标记 对TaskSiteInstance进行漏检标记
    public void checkYesterdayTask(){
        Set<String> yesterdayTasks = redisTemplate.opsForSet().members("TodayTasks");
        System.out.println("yesterdayTasks" + yesterdayTasks);
        if (yesterdayTasks == null || yesterdayTasks.isEmpty()) return;
        for(String taskinstance_id : yesterdayTasks){
            Set<String> taskSiteInstanceIDSet = redisTemplate.opsForSet().members(taskinstance_id);
            System.out.println("taskinstance_id:" + taskinstance_id);
            System.out.println("SiteInstanceIDSet:" + taskSiteInstanceIDSet);

            // 如果是已完成任务 就直接删了 如果是远古遗留痕迹 也直接删了
            if (taskSiteInstanceIDSet == null || taskSiteInstanceIDSet.isEmpty()){
                redisTemplate.delete("taskinstance_id");
                continue;
            }
            Map<String,Object> map2 = new HashMap<>();
            map2.put("taskinstance_id",taskinstance_id);
            if(taskInstanceMapper.findByCondition(map2).isEmpty()){
                redisTemplate.delete("taskinstance_id");
                continue;
            }

            // 将未完成的TaskSiteInstance进行漏检标记State:5
            for(String tasksiteinstance_id : taskSiteInstanceIDSet){
                Map<String,Object> map = new HashMap<>();
                map.put("tasksiteinstance_id",Integer.parseInt(tasksiteinstance_id));
                TaskSiteInstance taskSiteInstance = taskSiteInstanceMapper.findByCondition(map).get(0);
                taskSiteInstance.setState(5);
                taskSiteInstanceMapper.update(taskSiteInstance);
            }
            // 将对应的taskInstance设置为超时State:2
            Map<String,Object> map1 = new HashMap<>();
            map1.put("taskinstance_id",Integer.parseInt(taskinstance_id));
            TaskInstance taskInstance = taskInstanceMapper.findByCondition(map1).get(0);
            taskInstance.setState(2);
            taskInstanceMapper.update(taskInstance);
            // 清除掉对应的taskInstance缓存
            redisTemplate.delete(taskinstance_id);
        }
        // 完成所有检查工作后 清除掉上一日的缓存
        redisTemplate.delete("TodayTasks");
    }

    // 根据传入的点位的Last_Check_Time和Frequency信息 计算设置Next_Check_Time
    public void Next_Check_Date(Site site){
        List<Integer> frequency = site.getFrequency();
        int interval = frequency.get(0);
        int times = frequency.get(1);
        // 取得所有过去interval天内的TaskSiteInstance的数量
        int pastTimes = taskSiteInstanceMapper.countTimesByInterval(interval);
        // 如果巡检次数不足 那么下一次巡检就是明天
        if(pastTimes < times) site.setNext_check_date(LocalDate.now().plusDays(1));
        // 如果巡检次数充足 那么下一次巡检就是暂停interval-times天后的一天 然而 假如interval-times为0 那么巡检员就永无天日了
        if(pastTimes >= times) site.setNext_check_date(LocalDate.now().plusDays(interval - times + 1));
        siteMapper.updateTimes(site);
    }
}
