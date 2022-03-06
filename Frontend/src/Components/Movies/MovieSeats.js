import { Component } from "react";
import "../Admin/Admin.css";
import Navbar from "../Navbar/Navbar";
import axios from "axios";
import {BASEURL} from "../../Constants/BaseUrl";
import $ from 'jquery'; 


class MovieSeats extends Component{
    constructor(){
        super();
        this.state = {
            seats: [],
            reserved: [],
            id: ""
        }

        this.markSeat=this.markSeat.bind(this);
        this.reserve=this.reserve.bind(this);
        this.redirect=this.redirect.bind(this)
    }

    componentDidMount() {
        window.scrollTo(0, 0)
        let movie = this.props.location.state
        this.setState({seats: movie.seats.split("")})
        this.setState({id: movie.id})

    }

    markSeat(index){
        if(localStorage.getItem('loginType') == "ROLE_CLIENT"){
            var element = document.getElementById(index)
            element.classList.toggle("bg-white");
            element.classList.toggle("bg-success");
            if(element.className.includes("bg-white")){
                var array = this.state.reserved;
                const removedIndex = array.indexOf(index);
                array.splice(removedIndex, 1);
                this.setState({ reserved: array})
            }
            else{
                this.setState({ reserved: [...this.state.reserved, index] })
            }
        }
        else if(localStorage.getItem('loginType') == "ROLE_GUEST"){
            window.$('#exampleModalCenter2').modal('show')
        }
    }
    redirect(){
        window.location.replace("/login")
    }

    reserve(){
        if(document.getElementById("card-number").value.toString().length == 16 && document.getElementById("card-pin").value.toString().length == 3){
            axios.post(BASEURL+'/Movies/Reservation', this.state.reserved ,
                {
                    headers: {
                        'Authorization': 'Bearer ' + localStorage.getItem('token')
                    },
                    params: {
                        Movie: this.state.id
                    }
                }
                )   
                .then(res => {
                    if(res.status==200){
                        
                        alert("Tickets Reserved!")
                    }
                }).catch(err=>{
                    alert(err.response.data)
                })
        }
        else {
            alert("Enter Valid Card Information!")
        }
    }
    render(){
        return(
        <div id="movie-seats">
            <Navbar/>
            <div className="container">
                <div className="screen bg-white text-center mt-4"><h1 className="screen-title">Screen</h1></div>
                {
                        this.state.seats.map((seat,index)=>(
                            <>
                                {
                                    seat == 1
                                    ?
                                        <div className="seat bg-dark"></div>
                                    :
                                        <div id={index} className="seat bg-white" onClick={this.markSeat.bind(this,index)}></div>
                                }
                            </>
                        ))
                }
                {
                    localStorage.getItem('loginType') == "ROLE_CLIENT" ?
                        <div className="row justify-content-center p-3">
                            <button className="btn btn-dark btn-lg" data-toggle="modal" data-target="#exampleModalCenter">Reserve</button>
                        </div>
                    :
                        ""
                }
                <div className="modal fade" id="exampleModalCenter" tabIndex="-1" role="dialog" aria-labelledby="exampleModalCenterTitle" aria-hidden="true">
                    <div className="modal-dialog modal-dialog-centered" role="document">
                        <div className="modal-content bg-dark text-white">
                            <div className="modal-header">
                                <h5 className="modal-title" id="exampleModalLongTitle">Payment</h5>
                                <button type="button" className="close" data-dismiss="modal" aria-label="Close">
                                    <span aria-hidden="true" >&times;</span>
                                </button>
                            </div>
                            <div className="modal-body">
                                <label for="cardNumber">
                                    <h6>Card number</h6>
                                </label>
                                <div className="input-group"> 
                                    <input id="card-number" type="number" className="cardNumber" placeholder="0000 0000 0000 0000" className="form-control " required></input>
                                    <div className="input-group-append"> <span className="input-group-text text-muted"> <i className="fab fa-cc-visa mx-1"></i> <i class="fab fa-cc-mastercard mx-1"></i> <i class="fab fa-cc-amex mx-1"></i> </span> </div>
                                </div>
                                <label for="pin">
                                    <h6 className="mt-2">Card Pin</h6>
                                </label>
                                <div className="input-group"> 
                                    <input id="card-pin" type="number" className="pin" placeholder="123" className="form-control " required></input>
                                </div>
                            </div>
                            <div className="modal-footer">
                                <button type="button" className="btn btn-danger" data-dismiss="modal" onClick={this.reserve.bind(this)}>Confirm Payment</button>
                            </div>
                        </div>
                    </div>
                </div>
                <div className="modal fade" id="exampleModalCenter2" tabIndex="-1" role="dialog" aria-labelledby="exampleModalCenterTitle" aria-hidden="true">
                    <div className="modal-dialog modal-dialog-centered" role="document">
                        <div className="modal-content bg-dark text-white">
                            <div className="modal-header">
                                <h5 className="modal-title" id="exampleModalLongTitle">Reservations</h5>
                                <button type="button" className="close" data-dismiss="modal" aria-label="Close">
                                    <span aria-hidden="true" >&times;</span>
                                </button>
                            </div>
                            <div className="modal-body">
                                Do you want to signin to be able to reserve seats?
                            </div>
                            <div className="modal-footer">
                                <button type="button" className="btn btn-secondary" data-dismiss="modal">No</button>
                                <button type="button" className="btn btn-danger" onClick={this.redirect.bind(this)}>Yes</button>
                            </div>
                        </div>
                    </div>
                </div>

            </div>
        </div>
        )
    }
}
export default MovieSeats;