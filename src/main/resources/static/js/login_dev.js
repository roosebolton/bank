import {authenticateDev} from "./modules/authModule.js";

let login_data = {username:"admin@admin.com",
    password: 1234};

document.querySelector('#login').addEventListener('click', (e)=> {
        authenticateDev(login_data);
    }
)
