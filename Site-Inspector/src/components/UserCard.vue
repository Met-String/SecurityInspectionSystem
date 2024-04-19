<script setup>
import {userManager} from '@/api/api.js';
const props = defineProps({
    user:{
        type : Object,
        required : false
    }
})
</script>

<template>
    <!-- 展示被选中的用户信息 -->
    <div style="margin: 5px 0;">
        <el-row :gutter="20">
            <el-col :span = "6">
                <img v-if="user" :src="userManager.getAvatarURL(user.user_id)" class="avatar">
                <!-- <img v-if="user" :src="user.image_url" class="avatar"> -->
                <img v-if="!user" :src="userManager.getDefaultAvatar()" class="avatar">
            </el-col>
            <el-col :span = "18">
                <el-skeleton v-if="!user" :rows="3" />
                <el-descriptions
                direction="vertical"
                :column="2">
                    <div v-if="user">
                        <el-descriptions-item label="姓名">{{user.userName}}</el-descriptions-item>
                        <el-descriptions-item label="联系方式">{{user.phoneNumber}}</el-descriptions-item>
                        <el-descriptions-item label="职务" >{{user.position}}</el-descriptions-item>
                        <el-descriptions-item label="部门" >{{user.department}}</el-descriptions-item>
                    </div>
                </el-descriptions>
            </el-col>
        </el-row>
    </div>
</template>

<style scoped>
.avatar {
    border: 1px solid #cdcdcd;
    border-radius: 5px;
    width: 100%;
    object-fit: contain;
    border-radius:5%
}
</style>