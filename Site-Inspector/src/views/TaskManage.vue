<script setup>
import SimpleSitesList from '@/components/SimpleSitesList.vue';
import {reactive, ref, watch} from 'vue';
import {siteManager, taskManager} from '@/api/api.js';
import {userManager} from '@/api/api.js';
import MapContainer from '@/components/MapContainer.vue';
import {
  Check,
  Delete,
  Edit,
  Unlock,
  Lock,
  Message,
  Search,
  Close,
  Star,
} from '@element-plus/icons-vue'
let tasks = ref([]);

const getTask = () => {
    taskManager.getAllTasks().then(response => {
        tasks.value = response.data.data;
        console.log(tasks.value);
    })
}
getTask();

// Table的展开控制
const expandRowKeys = ref([])


// 点位池的加载 展开行时加载对应点位池
const sitesPools = reactive({});
const handleExpandChange = (row, expandedRows) => {
    // 如果实际上是关闭 那么取消掉expandRowKeys里的值
    if(expandRowKeys.value.filter(task_id => task_id == row.task_id) != ''){
        expandRowKeys.value = expandRowKeys.value.filter(
            task_id => task_id != row.task_id
        )
    }else{
        expandRowKeys.value.push(row.task_id)
    }
    
    // 如果这行的详细信息还未加载 加载详细信息
    if (!sitesPools[row.task_id]) { 
        taskManager.getSitesPool(row.task_id).then(response => {
            sitesPools[row.task_id] = response.data.data;
            console.log(sitesPools[row.task_id])
        });
    }
};

watch(sitesPools, (newValue, oldValue) => {
  console.log(`sitesPools changed`);
});

const getTagType = (state) => {
  switch (state) {
    case '正常':
      return 'success'; 
    case '异常':
      return 'warning';   
    case '损坏':
      return 'danger'; 
    case '失效':
      return 'info';
    default:
      return 'success'; 
  }
};

// 【获取今日任务实例相关】
const taskInstances = reactive([]);
const today = new Date();
const formattedDate = today.toISOString().split('T')[0];
const getTaskInstance = (task_id)=> {
    if(taskInstances[task_id]){
        return taskInstances[task_id];
    }
    taskManager.getTaskInstance({
        task_id : task_id,
        timestamp : formattedDate
    }).then(response => {
        taskInstances[task_id] = response.data.data
        // 附加一些统计信息
        taskInstances[task_id].forEach(taskInstance => {
            taskInstance.finish = taskInstance.taskSiteInstances.filter(taskSiteInstance =>
                taskSiteInstance.state != 0).length;
            taskInstance.normal = taskInstance.taskSiteInstances.filter(taskSiteInstance =>
                taskSiteInstance.state == 1).length;
            taskInstance.error = taskInstance.taskSiteInstances.filter(taskSiteInstance =>
                taskSiteInstance.state == 2 || taskSiteInstance.state == 3).length;    
            taskInstance.jump = taskInstance.taskSiteInstances.filter(taskSiteInstance =>
                taskSiteInstance.state == 4).length;
        });

        console.log(taskInstances[task_id])
    })
}

// 【获取用户信息相关】
const users = reactive([]);
const getAllUsers = () => {
    userManager.getAllUsers()
        .then(response => {
            response.data.data.forEach(user => {
                users[user.user_id] = user
            })
            console.log(users)
        })
}
getAllUsers()
// // 用户默认头像获取
const handleImageError = (event) =>{
    event.target.onerror = null;
    event.target.src = userManager.getDefaultAvatar();
}

// 【获取未分配点位相关】
const notBeAssignedSites = ref([])
const getNotBeAssignedSites = () =>{
    siteManager.getNotBeAssignedSites().then(response => {
        notBeAssignedSites.value = response.data.data
        console.log(response.data.data)
    })
}
getNotBeAssignedSites();


// 【编辑任务&点位池相关】
const temp_editedSitePool = ref([])
let editedTask = reactive({
    task_id : -1,
    name : '',
    user_id : '',
    description : '',
    state : '',
    sites : []
})
// 进入编辑态
const editTask = (task) => {
    // 如果点位池还未加载 那么先获得点位池 
    // 如果对应行还未展开 那么展开对应行
    if(!sitesPools[task.task_id]){
        taskManager.getSitesPool(task.task_id).then(response => {
            sitesPools[task.task_id] = response.data.data;
            console.log(sitesPools[task.task_id])
            temp_editedSitePool.value = [...sitesPools[task.task_id]];
        });
    }else{
        temp_editedSitePool.value = [...sitesPools[task.task_id]];
    }
    if(expandRowKeys.value.filter(task_id => task_id == task.task_id) == ''){
        expandRowKeys.value.push(task.task_id)
    }
    // 把待修改信息赋值给一批临时变量
    editedTask.task_id = task.task_id;
    Object.keys(task).forEach(key => {
        editedTask[key] = task[key];        
    });
}
// 取消编辑
const cancelEdit = () =>{
    editedTask.task_id = -1
    getNotBeAssignedSites();
    temp_SitePool_New.value = [];
}
// 将点位移出点位池
const siteRemove = (removedSite) => {
    temp_editedSitePool.value = temp_editedSitePool.value.filter(
        site => site.site_id != removedSite.site_id
    )
    notBeAssignedSites.value.push(removedSite);
    editedTask.sites = editedTask.sites.filter(
        site => site != removedSite.site_id
    )
}
// 将点位通过拖动移入点位池
const startSiteDrag = (event,addedSite) => {
    console.log(addedSite);
    event.dataTransfer.dropEffect = 'move';
    event.dataTransfer.effectAllowed = 'move';
    event.dataTransfer.setData("addedSite", JSON.stringify(addedSite));
}
const onDrop = (event) => {
    const addedSite = JSON.parse(event.dataTransfer.getData('addedSite'));
    console.log(addedSite);
    notBeAssignedSites.value = notBeAssignedSites.value.filter(
        site => site.site_id != addedSite.site_id
    )
    temp_editedSitePool.value.push(addedSite)
    editedTask.sites.push(addedSite.site_id);
}

// 【激活任务相关】
const activateTask = (task) => {
    task.state = 1;
    taskManager.activateTask(task).then(response => {
        console.log(response.data)
    })
}
const inactivate = (task) => {
    task.state = 0;
    taskManager.activateTask(task).then(response => {
        console.log(response.data)
    })
}


// 【删除任务相关】
const deleteTask = (task) => {
    console.log(task);
    taskManager.deleteTask(task).then(response =>{
        console.log(response.data.message)
        temp_SitePool_New.value = [];
        getNotBeAssignedSites();
        getTask();
    })
}


// 【新建任务相关】
const activeName = ref('check')
let newTask = reactive({
    task_id : 0,
    name : '',
    user_id : -1,
    description : '',
    state : '',
    sites : []
})

let newTaskUser = ref({
    user_id : -1
})

const temp_SitePool_New = ref([])
const siteRemove_New = (removedSite) => {
    temp_SitePool_New.value = temp_SitePool_New.value.filter(
        site => site.site_id != removedSite.site_id
    )
    notBeAssignedSites.value.push(removedSite);
    newTask.sites = newTask.sites.filter(
        site => site != removedSite.site_id
    )
}
const onDrop_New = (event) => {
    const addedSite = JSON.parse(event.dataTransfer.getData('addedSite'));
    notBeAssignedSites.value = notBeAssignedSites.value.filter(
        site => site.site_id != addedSite.site_id
    )
    temp_SitePool_New.value.push(addedSite);
    newTask.sites.push(addedSite.site_id);
}
const submitNewTask = () => {
    taskManager.createNewTask(newTask).then(response => {
        console.log(response.data.message);
        getTask();
        newTask.name = '';
        newTask.user_id = '';
        newTask.description = '';
        temp_SitePool_New.value = [];
    })
}
// 地图相关控制
const getMapCoord = (Coord) => {
    newSite.longitude = Coord.lng;
    newSite.latitude = Coord.lat;
    console.log(Coord);
}

</script>

<template>
    <el-row :gutter = "10">
        <!-- Task信息的基本展示和修改 左半区 -->
        <el-col :span="12" >
            <el-row :gutter = "10">
                <el-col :span="18">
                    <h3 style="text-align: center;background-color:bisque;margin: 5px 0px">任务管理</h3>
                <!-- 切换任务查看&调整和新建任务 -->
                <!-- {{ expandRowKeys }} -->
                <!-- {{ users[newTask.user_id]}} -->
                <el-tabs 
                    v-model="activeName"
                >
                    <el-tab-pane label="查看&调整" name="check">
                        <el-table 
                            :data="tasks" 
                            @expand-change = "handleExpandChange"
                            row-key = "task_id"
                            :expand-row-keys = "expandRowKeys" 
                            stripe
                            style="width: 100%; height:500px; margin:10px 0" >
                            <el-table-column type="expand">
                                <template #default = 'props'>
                                    <!-- 修改Task信息的模块 仅仅在和editedTask.task_id一致时显示 -->
                                    <div v-if="editedTask.task_id === props.row.task_id">
                                        <el-form
                                            labelWidth="auto"
                                            :model="editedTask"
                                            style="max-width: 600px"
                                        >
                                            <el-form-item label="任务名称">
                                                <el-input v-model="editedTask.name"/>
                                            </el-form-item>
                                            <el-form-item label="任务描述">
                                                <el-input v-model="editedTask.description" type="textarea" />
                                            </el-form-item>
                                            <el-form-item label="负责人">
                                                <el-select v-model="editedTask.user_id">
                                                    <el-option v-for="user in users" :label="user.userName" :value="user.user_id" />
                                                </el-select>
                                                <div>
                                                    <img :src="userManager.getAvatarURL(editedTask.user_id)" style="width: 20%; object-fit: contain;">
                                                </div>
                                            </el-form-item>
                                            <!-- {{ editedTask }} -->
                                            <!-- 对点位池的修改 占据了修改模块的一半 -->
                                            <h3 style="text-align: center;background-color:bisque;margin: 5px 0px">点位池</h3>
                                            <div 
                                                @drop="onDrop($event)"
                                                @dragenter.prevent
                                                @dragover.prevent
                                                class="sitesPool"
                                                style="margin: 10px;height:100%;">
                                                <el-tag v-for="site in temp_editedSitePool" 
                                                    :key="site.site_id"
                                                    :type="getTagType(site.state)"
                                                    closable
                                                    draggable="true"
                                                    @close="siteRemove(site)"
                                                    style="margin: 5px 5px;">
                                                    {{site.name}}
                                                </el-tag>
                                            </div>
                                        </el-form>
                                    </div>
                                    <!-- Task的展开列 -->
                                    <div v-if="editedTask.task_id != props.row.task_id">
                                        <h3 style="text-align: center;background-color:bisque;margin: 5px 0px">描述</h3>
                                            <div style="margin: 10px;">
                                                {{ props.row.description }}
                                            </div>
                                        <h3 style="text-align: center;background-color:bisque;margin: 5px 0px">今日进度</h3>
                                            <div style="margin: 10px;" v-for="taskInstance in getTaskInstance(props.row.task_id)">
                                                任务ID：{{ taskInstance.taskinstance_id }}
                                                任务进度：{{ taskInstance.finish }} / {{ taskInstance.taskSiteInstances.length }}
                                                其中：正常{{ taskInstance.normal }} 异常{{ taskInstance.error }} 跳检{{ taskInstance.jump }}      
                                            </div>
                                        <h3 style="text-align: center;background-color:bisque;margin: 5px 0px">负责人</h3>
                                        <div style="margin: 10px;">
                                            <el-row :gutter="20">
                                                    <el-col :span = "6">
                                                        <img :src="userManager.getAvatarURL(props.row.user_id)" style="width: 100%; object-fit: contain; border-radius:5%">
                                                    </el-col>
                                                    <el-col :span = "18">
                                                        <el-descriptions
                                                        direction="vertical"
                                                        :column="2">
                                                            <el-descriptions-item label="姓名">{{users[props.row.user_id].userName}}</el-descriptions-item>
                                                            <el-descriptions-item label="联系方式">{{users[props.row.user_id].phoneNumber}}</el-descriptions-item>
                                                            <el-descriptions-item label="职务" >{{users[props.row.user_id].position}}</el-descriptions-item>
                                                            <el-descriptions-item label="部门" >{{users[props.row.user_id].department}}</el-descriptions-item>
                                                        </el-descriptions>
                                                    </el-col>
                                            </el-row>
                                        </div>
                                        <h3 style="text-align: center;background-color:bisque;margin: 5px 0px">点位池</h3>
                                        <!-- 展示点位池 -->
                                        <div class="sitesPool" style="margin: 5px 0px;">
                                            <el-tag v-for="site in sitesPools[props.row.task_id]"
                                                :key="site.site_id"
                                                :type="getTagType(site.state)"
                                                style="margin: 5px 5px;">
                                                {{site.name}}
                                            </el-tag>
                                        </div>
                                    </div>
                                </template>
                            </el-table-column>
                            <!-- 其它正常Task条目展示数据 -->
                            <el-table-column prop="name" label="任务名称" width="150" />
                            <el-table-column prop="user_name" label="负责人" width="80" />
                            <el-table-column label="状态" width="80">
                                <template #default = "props">
                                    <el-tag v-if="props.row.state === 1" style="color:rgb(0, 110, 236)">已激活</el-tag>
                                    <el-tag v-if="props.row.state === 0" type="info">未激活</el-tag>
                                </template>
                            </el-table-column>
                            <el-table-column label="操作" maxwidth="1500">
                                <template #default="props">
                                    <el-button v-if="editedTask.task_id !== props.row.task_id && props.row.state === 0" @click = "activateTask(props.row)" :icon="Unlock" size="small" color="#66b1ff" >激活</el-button>
                                    <el-button v-if="editedTask.task_id !== props.row.task_id && props.row.state === 1" @click = "inactivate(props.row)" :icon="Lock" size="small" color="#ebb563">中止</el-button>
                                    <el-button v-if="editedTask.task_id != props.row.task_id" @click = "editTask(props.row)" color="#a6a9ad" :icon="Edit" size="small"/>
                                    <el-button v-if="editedTask.task_id != props.row.task_id" @click = "deleteTask(props.row)" color="#F56C6C" :icon="Delete" size="small"/>
                                    <el-button v-if="editedTask.task_id === props.row.task_id" @click = "cancelEdit" type="info" :icon="Close" size="small" />
                                    <el-button v-if="editedTask.task_id === props.row.task_id" @click = "" type="success" :icon="Check" size="small" />
                                </template>
                            </el-table-column>
                        </el-table>
                    </el-tab-pane>

                    <!-- 新建任务的Tab 和上面的查看&修改对应 -->
                    <el-tab-pane label="新建任务" name="create">
                        <el-form
                            labelWidth="auto"
                            :model="newTask"
                            style="max-width: 1200px">
                            <el-form-item label="任务名称">
                                <el-input v-model="newTask.name"/>
                            </el-form-item>
                            <el-form-item label="任务描述">
                                <el-input v-model="newTask.description" type="textarea" />
                            </el-form-item>
                            <el-form-item label="负责人员">
                                <el-select v-model="newTask.user_id">
                                    <el-option v-for="user in users" :label="user.userName" :value="user.user_id" />
                                </el-select>
                            </el-form-item>
                            <!-- 展示被选中的用户信息 -->
                            <el-row :gutter="20">
                                <el-col :span = "6">
                                    <img :src="userManager.getAvatarURL(newTask.user_id)" @error="handleImageError($event)" style="width: 100%; object-fit: contain; border-radius:5%">
                                </el-col>
                                <el-col :span = "18">
                                    <el-skeleton v-if="!users[newTask.user_id]" :rows="3" />
                                    <el-descriptions
                                    direction="vertical"
                                    :column="2">
                                        
                                        <div v-if="users[newTask.user_id]">
                                            <el-descriptions-item label="姓名">{{users[newTask.user_id].userName}}</el-descriptions-item>
                                            <el-descriptions-item label="联系方式">{{users[newTask.user_id].phoneNumber}}</el-descriptions-item>
                                            <el-descriptions-item label="职务" >{{users[newTask.user_id].position}}</el-descriptions-item>
                                            <el-descriptions-item label="部门" >{{users[newTask.user_id].department}}</el-descriptions-item>
                                        </div>
                                    </el-descriptions>
                                </el-col>
                            </el-row>
                            <!-- 新建点位池 -->
                            <h3 style="text-align:center; background-color:bisque; margin: 5px 5px; ">点位池</h3>
                            <div 
                                @drop="onDrop_New($event)"
                                @dragenter.prevent
                                @dragover.prevent
                                class="sitesPool"
                                style="margin:10px; min-height:100px;">
                                <el-tag v-for="site in temp_SitePool_New"
                                    :key="site.site_id"
                                    :type="getTagType(site.state)"
                                    closable
                                    draggable="true"
                                    @close="siteRemove_New(site)"
                                    style="margin: 5px 5px;">
                                    {{site.name}}   
                                </el-tag>
                            </div>
                            <!-- 提交新Task -->
                            <el-button style="width: 100%;" @click="submitNewTask">创建</el-button>
                        </el-form>
                    </el-tab-pane>
                </el-tabs>
                </el-col>
                <el-col :span="6">
                    <!-- 未被分配的点位池 -->
                    <h3 style="text-align: center;background-color:bisque;margin: 5px 0px">未分配点位</h3>
                    <el-scrollbar height="100%" style="height:500px" class="sitesPool">
                        <el-tag v-if="editedTask.task_id == -1 && activeName != 'create'" v-for="site in notBeAssignedSites"
                            :key="site.site_id"
                            :type="getTagType(site.state)"
                            style="margin: 5px 5px;">
                            {{site.name}}
                        </el-tag>
                        
                        <el-tag v-if="editedTask.task_id != -1 || activeName === 'create'" v-for="site in notBeAssignedSites"
                            :key="site.site_id"
                            :type="getTagType(site.state)"
                            draggable="true"
                            @dragstart="startSiteDrag($event ,site)"
                            style="margin: 5px 5px;">
                            {{site.name}}
                        </el-tag>
                    </el-scrollbar>
                </el-col>
            </el-row>
        </el-col >
        <el-col :span = "12">
            <MapContainer @mapClicked="getMapCoord"></MapContainer>
        </el-col>
    </el-row>
</template>

<style scoped>
.sitesPool {
    border: 1px solid #a8caac;
    border-radius: 5px;
    background-color: #fdfffd;
}

</style>