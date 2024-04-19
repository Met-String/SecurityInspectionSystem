<script setup>
import {siteManager} from '@/api/api.js';
import {userManager} from '@/api/api.js';
import {ref, reactive} from 'vue';
import {useToast} from 'vue-toastification';
import {
  Check,
  Delete,
  Edit,
  Message,
  Search,
  Close,
  Star,
} from '@element-plus/icons-vue'
import MapContainer from '@/components/MapContainer.vue';
// 【地图控件相关】
const mapContainerRef = ref(null);
const LocateAll = () => {
    sites.value.forEach(
        site => mapContainerRef.value.createMarker([site.longitude,site.latitude], site.name)
    )
}



let sites = ref([]);
const NewSiteformRef = ref(null);
const toast = useToast();
const siteIDList = ref([]);
const getAllSites = () => {
    siteManager.getAllSites()
        .then(response => {
            sites.value = response.data.data;
            console.log(sites);
        });
}
getAllSites()

let editState = ref(false)
const editSite = () => {
    editState.value = true
}

const DeleteSite = () => {
    siteManager.deleteSites([oldSite.site_id]).then((response) =>{
        toast.success(response.data.message);
        getAllSites();
        oldSite.site_id = '';
        oldSite.name = '';
        oldSite.address = '';
        oldSite.longitude = '';
        oldSite.latitude = '';
        oldSite.remark = '';
    })
}

const oldSite = reactive({
    site_id : '',
    organization_id : 0,
    user_id:'',
    user_name: '',
    group_id : 0,
    name : '',
    address : '',
    longitude : '',
    latitude : '',
    state: '',
    usability: '',
    qrcode_serial_number : '',
    remark: '',
    frequency: [0,0,0],
    history: []
})

// 【单个点位操作相关】
const checktaskSiteInstanceImg = ref('');
const checkSite = (row, column, event) => {
    console.log(row)
    editState.value = false;
    Object.keys(oldSite).forEach(key => {
    oldSite[key] = row[key];
  });
    mapContainerRef.value.createMarker([oldSite.longitude,oldSite.latitude], oldSite.name)
    getHistory(oldSite.site_id)
}

const saveEdit = () => {
    editState.value = false;
    siteManager.editSite(oldSite).then((response) =>{
        toast.success(response.data.message);
        getAllSites();
    })
}

const newSite = reactive({
    site_id : '',
    organization_id : 0,
    group_id : 0,
    name : '',
    address : '',
    longitude : '',
    latitude : '',
    state: '正常',
    usability: '可用',
    qrcode_serial_number : '',
    remark: '',
    frequency: [0,0,0]
})

const getMapCoord = (Coord) => {
    newSite.longitude = Coord.lng;
    newSite.latitude = Coord.lat;
    console.log(Coord);
}

const addNewSite = () => {
    siteManager.addNewSite(newSite).then(response => {
        getAllSites();
        if(response.data.code == 0){
            toast.success(response.data.message);
        } 
        else{
            toast.error(response.data.message);
        }
        newSite.name = '';
        newSite.longitude = '';
        newSite.latitude = '';
        newSite.remark = '';
    });
}

// 【点位巡检历史相关】
const getHistory = (site_id) => {
    siteManager.gethistory(site_id).then(response => {
        oldSite.history = response.data.data;
        getValidInspectTimes()
    })
}
const getCheckState = (state) => {
  switch (state) {
    case 1:
        return '正常'; 
    case 2:
        return '新发异常';   
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
const getValidInspectTimes = () => {
    oldSite.ValidInspectTimes = oldSite.history.filter(
        taskSiteInstance => taskSiteInstance.state != 0 && taskSiteInstance.state != 5
    ).length
    console.log('卧槽', oldSite.ValidInspectTimes)
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
</script>


<template>
    <!-- 菜单条 -->
    <el-row :gutter = "10">
        <el-col :span="12">
            <!-- 左半区菜单条 -->
            <el-row :gutter = "10" style="margin: 5px 0 0 0; border: 1px solid #cdcdcd;">
                <el-col :span="24" style="margin: 5px 0">
                    <el-button @click = "getAllSites">
                        刷新
                    </el-button>
                    <el-button @click = "LocateAll">
                        地图标出全部点位
                    </el-button>
                </el-col>
            </el-row>
            <!-- 左半区点位管理主体 -->
            <el-row style="margin: 0">
            <el-col :span="24">
                <el-table 
                :data="sites" 
                @row-click="checkSite"
                stripe
                style="width: 100%; height:300px; margin:5px 0; border: 1px solid #cdcdcd;" >
                    <el-table-column prop="name" label="点位名称" width="180" />
                    <el-table-column label="运行状态" width="80" >
                        <template #default="scope">
                            <div>
                                <el-tag v-if="scope.row.state === '正常'" type="success">正常</el-tag>
                                <el-tag v-if="scope.row.state === '异常'" type="warning">异常</el-tag>
                                <el-tag v-if="scope.row.state === '损坏'" type="danger">损坏</el-tag>
                                <el-tag v-if="scope.row.state === '失效'" type="info">失效</el-tag>
                            </div>
                        </template>
                    </el-table-column>
                    <el-table-column prop="user_name" label="负责人" width="80"/>
                    <el-table-column label="可用性" width="80">
                        <template #default = "scope">
                            <el-tag v-if="scope.row.usability === '可用'" type="primary">可用</el-tag>
                            <el-tag v-if="scope.row.usability === '不可用'" type="info">不可用</el-tag>
                        </template>
                    </el-table-column>
                    <el-table-column prop="last_check_time" label="最后巡检时间" width="180"/>
                    <el-table-column prop="next_check_date" label="下个巡检日"/>
                </el-table>
            </el-col>
            </el-row>
            <!-- 左半区点位信息展示&编辑主体 -->
            <el-row style="margin: 0;">
            <el-col :span="24" >
                <el-tabs type="border-card" >
                    <el-tab-pane label="点位信息" style="height:225px">
                        <el-scrollbar>
                            <el-descriptions :title="oldSite.name" :column = 2 style="width:100%">
                                <template #extra v-if="oldSite.site_id">
                                    <el-button v-show="!editState" type="primary" :icon="Edit" circle @click = "editSite"/>
                                    <el-button v-if= "editState" type="success" :icon="Check" circle @click= "saveEdit" />
                                    <el-button v-if= "editState" type="info" :icon="Close" circle @click= "editState = false;" />
                                    <el-button type="danger" :icon="Delete" circle @click = "DeleteSite"/>
                                </template>
                                <el-descriptions-item label="点位地址" :span = 2>
                                    <el-input v-if="editState" v-model="oldSite.address" style="width: 30%;"></el-input>
                                    <span v-if="!editState">{{oldSite.address}}</span>
                                </el-descriptions-item>
                                <el-descriptions-item label="点位状态">
                                    <el-select
                                        v-show="editState"
                                        v-model="oldSite.state"
                                        placeholder="点位状态"
                                        style="width: 80px;"
                                    >
                                        <el-option
                                            v-for="item in ['正常','异常','损坏','失效']"
                                            :value="item"
                                        />
                                    </el-select>
                                    <span v-if="!editState">
                                        <el-tag v-if="oldSite.state === '正常'" type="success">正常</el-tag>
                                        <el-tag v-if="oldSite.state === '异常'" type="warning">异常</el-tag>
                                        <el-tag v-if="oldSite.state === '损坏'" type="danger">损坏</el-tag>
                                        <el-tag v-if="oldSite.state === '失效'" type="info">失效</el-tag>
                                    </span>
                                </el-descriptions-item>
                                <el-descriptions-item label="可用性">
                                    <el-switch 
                                        v-show="editState"
                                        v-model="oldSite.usability" 
                                        :active-value= "'可用'"
                                        :inactive-value= "'不可用'"
                                    />
                                    <span v-if="!editState">
                                        <el-tag v-if="oldSite.usability === '可用'" type="primary">可用</el-tag>
                                        <el-tag v-if="oldSite.usability === '不可用'" type="info">不可用</el-tag>
                                    </span>
                                </el-descriptions-item>
                                <el-descriptions-item label="巡检频率" :span = 2 style="display: flex; align-items: center;">
                                        <span>每</span>
                                        <span v-if="!editState">{{ oldSite.frequency[0] }}</span>
                                        <el-input v-if="editState" v-model="oldSite.frequency[0]" type="number" style="width: 60px; margin: 0 10px;"></el-input>
                                        <span>天中有</span>
                                        <span v-if="!editState">{{ oldSite.frequency[1] }}</span>
                                        <el-input v-if="editState" v-model="oldSite.frequency[1]" type="number" style="width: 60px; margin: 0 10px;"></el-input>
                                        <span>个巡检天，每个巡检天巡检</span>
                                        <span v-if="!editState">{{ oldSite.frequency[2] }}</span>
                                        <el-input v-if="editState" v-model="oldSite.frequency[2]" type="number" style="width: 60px; margin: 0 10px;"></el-input>
                                        <span>次</span>
                                </el-descriptions-item>
                                <el-descriptions-item v-if="!editState" label="负责人" :span = 2>
                                    {{oldSite.user_name}}
                                </el-descriptions-item>
                                <el-descriptions-item label="点位描述" :span = 2>
                                    <el-input v-if="editState" v-model="oldSite.remark" type="textarea" style="width: 60%;" :rows = "1"></el-input>
                                    <span v-if="!editState">{{oldSite.remark}}</span>
                                </el-descriptions-item>
                                <el-descriptions-item label="经度">{{oldSite.longitude}}</el-descriptions-item>
                                <el-descriptions-item label="纬度" :span = 1>{{oldSite.latitude}}</el-descriptions-item>
                            </el-descriptions>
                        </el-scrollbar>
                    </el-tab-pane>
                    <!-- Tab第二区域 巡检历史 -->
                    <el-tab-pane label="巡检历史" style="height:225px">
                        
                        <el-row>
                            <el-col :span="18">
                                <div>{{oldSite.name}}</div>
                                 至今有效巡检日：{{oldSite.ValidInspectTimes}}天
                                <div>最近巡检历史：</div>
                                <el-scrollbar height=160>
                                    <div v-for="taskSiteInstance in oldSite.history" >
                                        <div v-if="taskSiteInstance.state != 0" style="margin: 5px 5px 5px 0; border: 1px solid #c7c7c7;">
                                        <span style="display: flex; align-items: center;">
                                            【日期】{{ taskSiteInstance.check_time }}
                                            【负责人】{{ users[taskSiteInstance.user_id].userName }}
                                            【结果】{{ getCheckState(taskSiteInstance.state)}}
                                            <el-button v-if="taskSiteInstance.normalInspection && taskSiteInstance.normalInspection.image_url"
                                            @click="checktaskSiteInstanceImg = taskSiteInstance.normalInspection.image_url"
                                            type="small"
                                            style="margin:2px 0 0 10px"
                                            >
                                                查看图片
                                            </el-button>
                                        </span>
                                            <div>
                                                【描述】
                                                <span v-if="taskSiteInstance.normalInspection">
                                                    {{ taskSiteInstance.normalInspection.description }}
                                                </span>
                                            </div>
                                        </div>
                                    </div>
                                </el-scrollbar>
                            </el-col>
                            <el-col :span="6">
                                <el-scrollbar height="220px" style="border: 2px solid #c7c7c7;" >
                                        <img :src="checktaskSiteInstanceImg" style="width: 100%;">
                                </el-scrollbar>
                            </el-col>
                        </el-row>
                    </el-tab-pane>
                    
                    <!-- Tab第三区域 新建点位 -->
                    <el-tab-pane label="新建点位" style="height:225px;">
                        <el-scrollbar>
                            <el-row :gutter="10" style="width:100%">
                                <el-col :span="12">
                                    <h3 class="editTitle">点位名称</h3>
                                    <el-input v-model="newSite.name" />
                                    <h3 class="editTitle">点位地址</h3>
                                    <el-input v-model="newSite.address" />
                                </el-col>
                                <el-col :span="12">
                                    <h3 class="editTitle">可用性</h3>
                                    <div >
                                        <el-switch 
                                        v-model="newSite.usability" 
                                        :active-value= "'可用'"
                                        :inactive-value= "'不可用'" 
                                        />
                                        可用/不可用
                                    </div>
                                    <h3 class="editTitle">经纬度(点击地图自动填充)</h3>
                                        <el-row :gutter="5">
                                            <el-col :span="12"><el-input v-model="newSite.longitude" placeholder="经度"/></el-col>
                                            <el-col :span="12"><el-input v-model="newSite.latitude" placeholder="纬度"/></el-col>
                                        </el-row>    
                                </el-col>
                                <el-col :span="24">
                                    <h3 class="editTitle">点位状态</h3>
                                    <el-select v-model="newSite.state">
                                        <el-option
                                            v-for="item in ['正常','异常','损坏','失效']"
                                            :value="item"
                                        />
                                    </el-select>
                                    <h3 class="editTitle">巡检频率</h3>
                                    <div style="display: flex; align-items: center;">
                                        <span>每</span>
                                        <el-input v-model="newSite.frequency[0]" type="number" style="width: 60px; margin: 0 10px;"></el-input>
                                        <span>天中有</span>
                                        <el-input v-model="newSite.frequency[1]" type="number" style="width: 60px; margin: 0 10px;"></el-input>
                                        <span>个巡检天，每个巡检天巡检</span>
                                        <el-input v-model="newSite.frequency[2]" type="number" style="width: 60px; margin: 0 10px;"></el-input>
                                        <span>次</span>
                                    </div>
                                    <h3 class="editTitle">备注</h3>
                                    <el-input v-model="newSite.remark" type="textarea" />
                                    <el-button style="width: 100%;" @click="addNewSite">添加</el-button>
                                </el-col>
                            </el-row>
                        </el-scrollbar>
                    </el-tab-pane>
                </el-tabs>
            </el-col>
            </el-row>
        </el-col>

        <el-col :span="12">
            <MapContainer ref="mapContainerRef" @mapClicked="getMapCoord"></MapContainer>
        </el-col>
    </el-row>
</template>

<style scoped>
    .editTitle{
        text-align: center;
        background-color:rgb(196, 248, 255);
        margin: 5px 0px;
    }
</style>