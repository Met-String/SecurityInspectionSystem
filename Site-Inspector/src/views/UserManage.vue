<script setup>
import {ref} from 'vue';
import {userManager} from '@/api/api.js';
let users = ref([]);
let userHistory = ref([])
const getAllUsers = () => {
    userManager.getAllUsers()
        .then(response => {
            users.value = response.data.data;
            console.log(users)
        })
}
getAllUsers()

const checkUser = (row) => {
    userManager.getUserHistory(row.user_id).then(
        response => {
            userHistory.value = response.data.data;
            console.log(response.data.data); 
        })
}

const getCheckState = (state) => {
  switch (state) {
    case 0:
        return '进行中';
    case 1:
        return '正常'; 
    case 2:
        return '异常';
    case 3:
        return '异常跟踪';
    case 5:
        return '超时';   
    case 4:
        return '跳检'; 
    default:
        return '/'; 
  }
};
</script>


<template>
    <el-row :gutter = "10">
        <!-- 左半区用户列表 -->
        <el-col :span="12">
            <el-table 
                :data="users" 
                @row-click="checkUser"
                stripe
                style="width: 100%; height:640px; margin:10px 0" >
                <el-table-column prop="user_id" label="" width="180" >
                    <template #default="scope" >
                        <div class="square-image-container" style="width:100%; height:180px">
                            <img :src="userManager.getAvatarURL(scope.row.user_id)"  style="width: 100%; object-fit: cover;">
                            <!-- <img :src="scope.row.image_url" style="width: 100%; object-fit: cover;"> -->
                        </div>
                    </template>
                </el-table-column>
                <el-table-column prop="userName" label="姓名" width="180" />
                <el-table-column prop="phoneNumber" label="电话号" width="180" />
                <el-table-column prop="position" label="职位" width="180" />
            </el-table>
        </el-col>
        <el-col :span="12">
            <el-table :data="userHistory" style="width: 100%; height:650px">
                <el-table-column type="expand">
                  <template #default="props">
                    <div>
                        <h3 style="text-align: center;background-color:bisque;margin: 5px 0px">巡检点位</h3>
                        <el-table :data="props.row.taskSiteInstances">
                            <el-table-column type="expand">
                                <template #default="taskSiteInstance">
                                    <el-row :gutter="10">
                                        <el-col :span="18">
                                            <h3 style="text-align: center;background-color:bisque;margin: 5px 0px">情况描述</h3>
                                            <span v-if="taskSiteInstance.row.normalInspection">
                                                {{ taskSiteInstance.row.normalInspection.description }}
                                            </span>
                                        </el-col>
                                        <el-col :span="6">
                                            <el-scrollbar height="220px" style="border: 2px solid #c7c7c7;" >
                                                <img v-if="taskSiteInstance.row.normalInspection?.image_url" :src="taskSiteInstance.row.normalInspection.image_url"  style="width:100%">
                                            </el-scrollbar>
                                        </el-col>
                                    </el-row>
                                </template>
                            </el-table-column>
                            <el-table-column label="Name" prop="site_name" />
                            <el-table-column label="完成时间" prop="check_time" />
                            <el-table-column label="巡检结果">
                                <template #default="taskSiteInstance">
                                {{ getCheckState(taskSiteInstance.row.state) }} 
                                </template>
                            </el-table-column>
                      </el-table>
                    </div>
                  </template>
                </el-table-column>
                <el-table-column label="任务日期" prop="start_time" />
                <el-table-column label="完成情况">
                    <template #default = "props">
                        <span v-if="props.row.state === 1">完成</span>
                        <span v-if="props.row.state === 0">进行中</span>
                        <span v-if="props.row.state === 2">超时</span>
                    </template>
                </el-table-column>
                <el-table-column label="点位数量" prop="taskSiteInstances.length" />
            </el-table>
        </el-col>
    </el-row>
</template>