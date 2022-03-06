import React, { Component } from "react";
import '../Login/Login.css'
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
        this.signup=this.signup.bind(this);
        this.validate=this.validate.bind(this);
    }
    handleChange(field, e) {
        let fields = this.state.fields;
        fields[field] = e.target.value;
        this.setState({ fields });
    }
    validate(){
        if(Object.keys(this.state.fields).length != 7){
            alert("Please enter all fields")
            return false;
        }
        if(this.state.fields["password"]!=this.state.fields["repassword"]){
            alert("Two password doesn't match");
            return false;
        }
        if (!/\d/.test(this.state.fields["password"]) || !/[a-zA-Z]/.test(this.state.fields["password"])) {
            alert("Password Should contain at least 1 digit and 1 letter");
            return false;
        }
        if(this.state.fields["password"].length <= 8){
            alert("Password length should be greater than 8");
            return false;
        }
        return true;
    }
    signup(e){
        e.preventDefault();
        if(this.validate()){
            axios.post(BASEURL+'/users/signup',
            {
                headers: {
                    'Content-Type': 'application/json'
                },
                username:this.state.fields["username"],
                firstname:this.state.fields["firstname"],
                lastname:this.state.fields["lastname"],
                email:this.state.fields["email"],
                password:this.state.fields["password"],
                appUserRoles: [this.state.fields["type"]]
            }
            )   
            .then(res => {
                if(res.status==200){
                    if(this.state.fields["type"]=="ROLE_MANAGER"){
                        alert("Signed up correctly, wait until admin approves you as a manager.")
                    }
                    else {
                        alert("Signed up correctly")
                    }
                }
            }).catch(err=>{
                if (err.response.status==405) {
                    alert(err.response.data)
                }
            })
            }
    }

    render() {
        return (
            <div id="signup">
                <Navbar/>
                <div className="row justify-content-center mr-0 p-5">
                    <form className="form p-5 mh-75 col-lg-6 col-md-10 col-sm-10">
                        <h1 className="text-center text-white pb-3">Signup</h1>
                        <div className="form-group row justify-content-center p-1">
                            <input type="username" className="form-control col-lg-6 col-md-10 col-sm-10" placeholder="Username" onChange={this.handleChange.bind(this, "username")}></input>
                        </div>
                        <div className="form-group row justify-content-center p-1">
                            <input type="password" className="form-control col-lg-6 col-md-10 col-sm-10" placeholder="Password" onChange={this.handleChange.bind(this, "password")}></input>
                        </div>
                        <div className="form-group row justify-content-center p-1">
                            <input type="password" className="form-control col-lg-6 col-md-10 col-sm-10" placeholder="Repeat Password" onChange={this.handleChange.bind(this, "repassword")}></input>
                        </div>
                        <div className="form-group row justify-content-center p-1">
                            <input type="firstname" className="form-control col-lg-6 col-md-10 col-sm-10" placeholder="First Name" onChange={this.handleChange.bind(this, "firstname")}></input>
                        </div>
                        <div className="form-group row justify-content-center p-1">
                            <input type="lastname" className="form-control col-lg-6 col-md-10 col-sm-10" placeholder="Last Name" onChange={this.handleChange.bind(this, "lastname")}></input>
                        </div>
                        <div className="form-group row justify-content-center p-1">
                            <input type="email" className="form-control col-lg-6 col-md-10 col-sm-10" placeholder="Email" onChange={this.handleChange.bind(this, "email")}></input>
                        </div>
                        <div className="form-group row justify-content-center p-1">
                            <select class="form-control col-lg-6 col-md-10 col-sm-10" onChange={this.handleChange.bind(this, "type")}>
                                <option selected disabled value="">Select user type</option>
                                <option value="ROLE_CLIENT" >Client</option>
                                <option value="ROLE_MANAGER">Manager</option>
                            </select>
                        </div>
                        <div className="row justify-content-center p-3">
                            <button type="submit" className="btn btn-dark" onClick={this.signup.bind(this)}>Signup</button>
                        </div>
                    </form>
                </div>
            </div>
        );
    }
}
export default Login;