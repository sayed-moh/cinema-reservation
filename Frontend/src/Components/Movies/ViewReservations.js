import { Component } from "react";
import "../Admin/Admin.css";
import Navbar from "../Navbar/Navbar";
import axios from "axios";
import {BASEURL} from "../../Constants/BaseUrl";




class ViewReservations extends Component{
    constructor(){
        super();
        this.state = {
            reservations: []
        }
        this.cancel=this.cancel.bind(this);
    }
    componentDidMount() {
        axios.get(BASEURL+'/Movies/Get/Reservations',{
            headers: {
                'Authorization': 'Bearer ' + localStorage.getItem("token")
            }
        })  
        .then(res => {
            if(res.status==200){
                this.setState({reservations: res.data})
            }
        }).catch(err=>{
        })
    }
    cancel(id){
        axios.delete(BASEURL+'/Movies/Delete/Reservations',
            {
                headers: {
                    'Authorization': 'Bearer ' + localStorage.getItem("token"),
                    'Content-Type': 'application/json'
                },
                params: {
                    Res: id
                }
            }
            )   
            .then(res => {
                if(res.status===200){
                    alert("Reservation Deleted!")
                }
            }).catch(err=>{
                alert(err.response.data)
            })
    }
    render(){
        return(
            <div id="view-movies">
                <Navbar/>
                <div className="container">
                    <h1 className="section-header pt-5 pb-5 text-white">View My Reservations </h1>
                    <div className="d-flex justify-content-between flex-wrap">
                        {
                            this.state.reservations.length !== 0
                            ?
                            
                                this.state.reservations.map((reservation,index)=>(
                                    <>
                                        <div className="card text-white bg-dark mb-3 col-lg-5 col-sm-8 col-md-8">
                                            <div className="card-header">{reservation.movieTitle}</div>
                                            <div className="card-body">
                                                <h5 className="card-title">{reservation.movieDate}</h5>
                                                <p className="card-text">Tickets Reserved: {reservation.seatsCount}</p>
                                            </div>
                                            <div className="card-footer d-flex justify-content-between">
                                                <button type="button" className="btn btn-danger" data-toggle="modal" data-target={"#exampleModalCenter"+index}>Cancel</button>
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
                                                        Are you sure you want to cancel this reservation for movie: {reservation.movieTitle} ? 
                                                    </div>
                                                    <div className="modal-footer">
                                                        <button type="button" className="btn btn-secondary" data-dismiss="modal">No</button>
                                                        <button type="button" className="btn btn-danger" onClick={this.cancel.bind(this,reservation.id)}>Yes</button>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </>
                                ))
                            :
                                <div className="row justify-content-center" style={{textAlign:"center"}}>
                                    <h2 className="text-white">You have no reservations avaliable to show right now!</h2>
                                </div>
                        }
                    </div>
                </div>
            </div>
        );
    }
}
export default ViewReservations