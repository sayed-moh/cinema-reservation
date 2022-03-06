import { Component } from "react";
import './Admin.css';
import axios from "axios";
import Navbar from "../Navbar/Navbar";
import {BASEURL} from "../../Constants/BaseUrl";



class ApproveUser extends Component{
    constructor(){
        super();
        this.state = {
            users: []
        }
        this.approve=this.approve.bind(this);
    }
    componentDidMount(){
        axios.get(BASEURL+'/users/ApproveUser',{
            headers: {
                'Authorization': 'Bearer ' + localStorage.getItem("token")
            }
        })   
        .then(res => {
            if(res.status==200){
                this.setState({users: res.data})
            }
        }).catch(err=>{
            //alert(err.response.data)
        })
    }
    approve(username){
        axios.put(BASEURL+'/users/ApproveUser/update', {},
            {
                headers: {
                    'Authorization': 'Bearer ' + localStorage.getItem("token"),
                    'Content-Type': 'application/json'
                },
                params: {
                    username: username
                }
            }
            )   
            .then(res => {
                if(res.status===200){
                    alert("User Approved!")
                }
            }).catch(err=>{
                //alert(err.response)
            })
    }

    render() {
        return(
            <div id="approve-user">
                <Navbar/>
                <div className="container">
                    <h1 className="section-header pt-5 pb-5 text-white">Approve Users </h1>
                        {
                            this.state.users.length !== 0
                            ?
                            
                                this.state.users.map((user,index)=>(
                                    <>
                                        <div className="card d-inline-block text-white bg-dark mb-3 ml-3 mt-3 col-lg-5 col-sm-6 col-md-6">
                                            <div className="card-header">{user.userName}</div>
                                            <div className="card-body">
                                                <h5 className="card-title">{user.firstName} {user.lastName}</h5>
                                                <p className="card-text">{user.email}</p>
                                            </div>
                                            <div className="card-footer">
                                                <button type="button" className="btn btn-danger" data-toggle="modal" data-target={"#exampleModalCenter"+index}>Approve</button>
                                            </div>
                                        </div>
                                        <div className="modal fade" id={"exampleModalCenter"+index} tabIndex="-1" role="dialog" aria-labelledby="exampleModalCenterTitle" aria-hidden="true">
                                            <div className="modal-dialog modal-dialog-centered" role="document">
                                                <div className="modal-content bg-dark text-white">
                                                    <div className="modal-header">
                                                        <h5 className="modal-title" id="exampleModalLongTitle">User approval</h5>
                                                        <button type="button" className="close" data-dismiss="modal" aria-label="Close">
                                                            <span aria-hidden="true" >&times;</span>
                                                        </button>
                                                    </div>
                                                    <div className="modal-body">
                                                        Are you sure you want to approve {user.userName} as manager?
                                                    </div>
                                                    <div className="modal-footer">
                                                        <button type="button" className="btn btn-secondary" data-dismiss="modal">No</button>
                                                        <button type="button" className="btn btn-danger" onClick={this.approve.bind(this,user.userName)}>Yes</button>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </>
                                ))
                            :
                                <div className="row justify-content-center" style={{textAlign:"center"}}>
                                    <h2 className="text-white">You don't have any users to approve! </h2>
                                </div>
                        }
                </div>
            </div>
        );
    }
}
export default ApproveUser;