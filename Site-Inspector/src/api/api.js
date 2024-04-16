import axios from "axios"

const siteManager = {
    getAllSites : () => {
        return axios.get(domain + 'sites/')
    },
    getNotBeAssignedSites : () => {
        return axios.get(domain + 'sites/' + '?notBeAssigned=true')
    },
    addNewSite : (site) => {
        return axios.post(domain + 'sites/add', site)
    },
    deleteSites : (sitesIdList) => {
        return axios.delete(domain + 'sites/delete', { data: sitesIdList })
    },
    editSite : (site) => {
        return axios.post(domain + 'sites/edit', site)
    }
}

const userManager = {
    getAllUsers : () => {
        return axios.get(domain + 'user/get/all')
    },

    getUserHistory : (user_id) => {
        return axios.get(domain + `user/history?user_id=${user_id}`)
    }, 
    
    getAvatarURL : (user_id) => {
        return domain + `images/avatars/user${user_id}.jpg`
    },
    getDefaultAvatar : () => {
        return domain + "images/avatars/defaultAvatar.jpg"
    },

}

const taskManager = {
    getAllTasks : () => {
        return axios.get(domain + 'task/')
    },
    getSitesPool : (task_id) =>{
        return axios.get(domain + `task/sitespool/${task_id}`)
    },
    createNewTask : (task) => {
        return axios.post(domain + 'task/add', task)
    },
    activateTask : (task) => {
        return axios.post(domain + 'task/activate', task)
    },
    deleteTask : (task) => {
        return axios.delete(domain + 'task/delete', { data: task })
    },
    getTaskInstance : ({timestamp, user_id, task_id}) => {
        const params = new URLSearchParams();
        if(timestamp) params.append('timestamp',timestamp);
        if(user_id) params.append("user_id", user_id);
        if(task_id) params.append("task_id",task_id);
        return axios.get(domain + 'task/taskinstance', {params});
    }
}

const domain = 'http://localhost:8080/'

export {
    siteManager,
    userManager,
    taskManager,
    domain
}