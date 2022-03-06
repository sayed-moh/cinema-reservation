import { Component } from "react";
import './Admin.css';
import axios from "axios";
import Navbar from "../Navbar/Navbar";
import {BASEURL} from "../../Constants/BaseUrl";



class DeleteUser extends Component{
    constructor() {
        super();
        this.state = {
            username: ""
        };
        this.handleUsername=this.handleUsername.bind(this);
        this.deleteUser=this.deleteUser.bind(this);
    }
    handleUsername(e) {
        this.setState({username: e.target.value})
    }
    deleteUser(){
        if (this.state.username == ""){
            alert("Please enter Username");
            return;
        }
        axios.delete(BASEURL+'/users/deleteUser',
            {
                headers: {
                    'Authorization': 'Bearer ' + localStorage.getItem("token"),
                    'Content-Type': 'application/json'
                },
                params: {
                    username: this.state.username
                }
            }
            )   
            .then(res => {
                if(res.status===200){
                    alert("User Deleted!")
                }
            }).catch(err=>{
                alert(err.response.data)
            })

    }
    render(){
        return(
            <div id="delete-user">
                <Navbar/>
                <div className="container">
                    <h1 className="section-header pt-5 pb-5 text-white">Delete User </h1>
                    <div className="input-group mb-3 col-lg-4">
                        <div className="input-group-prepend">
                            <span className="input-group-text" id="basic-addon1">@</span>
                        </div>
                        <input type="text" className="form-control" placeholder="Username" aria-label="Username" aria-describedby="basic-addon1" id="delete-username" onChange={this.handleUsername}></input>
                    </div>
                    <button type="button" data-toggle="modal" data-target="#exampleModalCenter" className="btn btn-dark ml-3">Delete</button>
                </div>
                <div className="modal fade" id="exampleModalCenter" tabIndex="-1" role="dialog" aria-labelledby="exampleModalCenterTitle" aria-hidden="true">
                    <div className="modal-dialog modal-dialog-centered" role="document">
                        <div className="modal-content bg-dark text-white">
                            <div className="modal-header">
                                <h5 className="modal-title" id="exampleModalLongTitle">User approval</h5>
                                <button type="button" className="close" data-dismiss="modal" aria-label="Close">
                                    <span aria-hidden="true" >&times;</span>
                                </button>
                            </div>
                            <div className="modal-body">
                                Are you sure you want to delete user with Username: {this.state.username} ?
                            </div>
                            <div className="modal-footer">
                                <button type="button" className="btn btn-secondary" data-dismiss="modal">No</button>
                                <button type="button" className="btn btn-danger" onClick={this.deleteUser.bind(this)}>Yes</button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        );
    }
}
export default DeleteUser;