<script setup>
import {siteManager} from '@/api/api.js';
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
    frequency: [0,0,0]
})

const checkSite = (row, column, event) => {
    console.log(row)
    editState.value = false;
    Object.keys(oldSite).forEach(key => {
    oldSite[key] = row[key];
  });
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
    state: '可用',
    usability: '正常',
    qrcode_serial_number : '',
    remark: '',
    frequency: [0,0,0]
})

const rules = {
    name : [{required : true, message : '点位名称不能为空', trigger : 'blur'}],
    longitude : [{required : true, message : '经度不能为空', trigger : 'blur'}],
    latitude : [{required : true, message : '纬度不能为空', trigger : 'blur'}]
}

const getMapCoord = (Coord) => {
    newSite.longitude = Coord.lng;
    newSite.latitude = Coord.lat;
    console.log(Coord);
}

const addNewSite = () => {
    NewSiteformRef.value.validate((valid) => {
        if(valid){
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
    })
}
</script>


<template>
    <el-row :gutter = "10">
        <el-col :span="12">
            <el-button @click = "getAllSites">
                刷新
            </el-button>
            <el-table 
                :data="sites" 
                @row-click="checkSite"
                stripe
                style="width: 100%; height:400px; margin:10px 0" >
                <el-table-column prop="name" label="点位名称" width="180" />
                <el-table-column label="运行状态" width="160" >
                    <template #default="scope">
                        <div>
                            <el-tag v-if="scope.row.state === '正常'" type="success">正常</el-tag>
                            <el-tag v-if="scope.row.state === '异常'" type="warning">异常</el-tag>
                            <el-tag v-if="scope.row.state === '损坏'" type="danger">损坏</el-tag>
                            <el-tag v-if="scope.row.state === '失效'" type="info">失效</el-tag>
                        </div>
                    </template>
                </el-table-column>
                <el-table-column prop="user_name" label="负责人" width="180"/>
                <el-table-column label="可用性">
                    <template #default = "scope">
                        <el-tag v-if="scope.row.usability === '可用'" type="primary">可用</el-tag>
                        <el-tag v-if="scope.row.usability === '不可用'" type="info">不可用</el-tag>
                    </template>
                </el-table-column>
            </el-table>
        
            <el-tabs type="border-card">
                <el-tab-pane label="点位信息">
                    <el-descriptions :title="oldSite.name" :column = 2>
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
                        <el-descriptions-item label="负责人" :span = 2>
                            <el-input v-if="editState" v-model="oldSite.user_name" style="width: 30%;"></el-input>
                            <span v-if="!editState">{{oldSite.user_name}}</span>
                        </el-descriptions-item>
                        <el-descriptions-item label="点位描述" :span = 2>
                            <el-input v-if="editState" v-model="oldSite.remark" type="textarea" style="width: 60%;" :rows = "1"></el-input>
                            <span v-if="!editState">{{oldSite.remark}}</span>
                        </el-descriptions-item>
                        <el-descriptions-item label="经度">{{oldSite.longitude}}</el-descriptions-item>
                        <el-descriptions-item label="纬度" :span = 1>{{oldSite.latitude}}</el-descriptions-item>
                      </el-descriptions>
                </el-tab-pane>
        
                <el-tab-pane label="新建点位">
                    <el-form ref = 'NewSiteformRef' :model = 'newSite' :rules = 'rules' label-width="auto" style="max-width: 600px">
                        <el-form-item label="点位名称" prop = "name">
                            <el-input v-model="newSite.name" />
                        </el-form-item>
                        <el-form-item label="点位地址" prop = "address">
                            <el-input v-model="newSite.address" />
                        </el-form-item>
                        <el-form-item label="经度" prop = "longitude">
                            <el-input v-model="newSite.longitude" />
                        </el-form-item>
                        <el-form-item label="纬度" prop = "latitude">
                            <el-input v-model="newSite.latitude" />
                        </el-form-item>
                        <el-form-item label="可用性">
                            <el-switch 
                                v-model="newSite.usability" 
                                :active-value= "'可用'"
                                :inactive-value= "'不可用'" 
                            />
                        </el-form-item>

                        <el-form-item label="点位状态">
                            <el-select
                                v-model="newSite.state"
                                placeholder="点位状态"
                                style="width: 80px;"
                            >
                            <el-option
                                v-for="item in ['正常','异常','损坏','失效']"
                                :value="item"
                            />
                            </el-select>
                        </el-form-item>
                        
                        <el-form-item label="描述">
                            <el-input v-model="newSite.remark" type="textarea" />
                        </el-form-item>
                        <el-form-item label="巡检频率">
                            <div style="display: flex; align-items: center;">
                                <span>每</span>
                                <el-input v-model="newSite.frequency[0]" type="number" style="width: 60px; margin: 0 10px;"></el-input>
                                <span>天中有</span>
                                <el-input v-model="newSite.frequency[1]" type="number" style="width: 60px; margin: 0 10px;"></el-input>
                                <span>个巡检天，每个巡检天巡检</span>
                                <el-input v-model="newSite.frequency[2]" type="number" style="width: 60px; margin: 0 10px;"></el-input>
                                <span>次</span>
                            </div>
                        </el-form-item>
                        <el-form-item>
                            <el-button type="primary" @click="addNewSite">
                                添加
                            </el-button>
                        </el-form-item>
                    </el-form>
                </el-tab-pane>
            </el-tabs>
        </el-col>

        <el-col :span="12">
            <MapContainer @mapClicked="getMapCoord"></MapContainer>
        </el-col>
      </el-row>

</template>