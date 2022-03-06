import React, { Component } from "react";
import './Login.css';
import axios from "axios";
import Navbar from "../Navbar/Navbar";
import {BASEURL} from "../../Constants/BaseUrl";

class Login extends Component {
    constructor(){
        super();
        this.state = {
            fields: {}
        };
        this.handleChange=this.handleChange.bind(this);
        this.login=this.login.bind(this);
    }
    handleChange(field, e) {
        let fields = this.state.fields;
        fields[field] = e.target.value;
        this.setState({ fields });
    }
    login(e){
        e.preventDefault();
        axios.post(BASEURL+'/users/signin', null ,
        {
            params: {
                username:this.state.fields["username"],
                password:this.state.fields["password"]
            }
        }
        )   
        .then(res => {
            if(res.status==200){
                localStorage.setItem('loginType', res.data.role.slice(1,-1));
                localStorage.setItem('token', res.data.token);
                window.location.replace("/");
            }
        }).catch(err=>{
            if (err.response.status==404) {
                alert(err.response.data)
            }
        })
    }
    render() {
        return (
            <div id="login" >
                <Navbar/>
                <div className="row justify-content-center mr-0 p-5">
                    <form className="form p-5 h-50 col-lg-6 col-md-10 col-sm-10">
                        <h1 className="text-center text-white pb-3">Login</h1>
                        <div className="form-group row justify-content-center p-1">
                            <input type="username" className="form-control col-lg-6 col-md-10 col-sm-10" placeholder="Username" onChange={this.handleChange.bind(this, "username")}></input>
                        </div>
                        <div className="form-group row justify-content-center p-1">
                            <input type="password" className="form-control col-lg-6 col-md-10 col-sm-10" placeholder="Password" onChange={this.handleChange.bind(this, "password")}></input>
                        </div>
                        <div className="row justify-content-center p-3">
                            <button type="submit" className="btn btn-dark" onClick={this.login.bind(this)}>Login</button>
                        </div>
                    </form>
                </div>
            </div>
        );
    }
}
export default Login;