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
</script>


<template>
    <el-row :gutter = "10">
        <el-col :span="12">
            <el-table 
                :data="users" 
                @row-click="checkUser"
                stripe
                style="width: 100%; height:400px; margin:10px 0" >
                <el-table-column prop="user_id" label="" width="180" >
                    <template #default="scope" >
                            <img :src="userManager.getAvatarURL(scope.row.user_id)" style="width: 100%; object-fit: contain;">
                    </template>
                </el-table-column>
                <el-table-column prop="userName" label="姓名" width="180" />
                <el-table-column prop="phoneNumber" label="电话号" width="180" />
                <el-table-column prop="position" label="职位" width="180" />
            </el-table>
        </el-col>
        <el-col :span="12">
            <el-table :data="userHistory" style="width: 100%">
                <el-table-column type="expand">
                  <template #default="props">
                    <div>
                      <h3>巡检点位</h3>
                      <el-table :data="props.row.taskSiteInstances">
                        <el-table-column type="expand">
                            <template #default="taskSiteInstance">
                                {{ taskSiteInstance.row.normalInspection.description }}
                                {{ taskSiteInstance.row.normalInspection.image_url }}
                            </template>
                        </el-table-column>
                        <el-table-column label="Name" prop="site_name" />
                        <el-table-column label="完成状态" prop="state" />
                        <el-table-column label="完成时间" prop="check_time" />
                        <el-table-column label="巡检结果" prop="normalInspection.anomaly" />
                      </el-table>
                    </div>
                  </template>
                </el-table-column>
                <el-table-column label="任务日期" prop="start_time" />
                <el-table-column label="完成情况">
                    <template #default = "props">
                        <span v-if="props.row.end_time">已完成</span>
                        <span>进行中</span>
                        <span v-if="!props.row.end_time">未完成</span>
                    </template>
                </el-table-column>
                <el-table-column label="点位数量" prop="taskSiteInstances.length" />
              </el-table>
        </el-col>
    </el-row>
</template>