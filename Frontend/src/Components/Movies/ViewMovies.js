import { Component } from "react";
import "../Admin/Admin.css";
import { Link } from "react-router-dom";
import Navbar from "../Navbar/Navbar";
import axios from "axios";
import {BASEURL} from "../../Constants/BaseUrl";




class ViewMovies extends Component{
    constructor(){
        super();
        this.state = {
            movies: []
        }
    }
    componentDidMount() {
        axios.get(BASEURL+'/Movies/Movie/Get')   
        .then(res => {
            if(res.status==200){
                this.setState({movies: res.data})
            }
        }).catch(err=>{
        })
        console.log(localStorage.getItem("loginType"))
    }
    render(){
        return(
            <div id="view-movies">
                <Navbar/>
                <div className="container">
                    <h1 className="section-header pt-5 pb-5 text-white">View Movies </h1>
                    <div className="d-flex justify-content-between flex-wrap">
                        {
                            this.state.movies.length !==0
                            ?
                            
                                this.state.movies.map((movie)=>(
                                    <>
                                        <div className="card text-white bg-dark mb-3 col-lg-5 col-sm-8 col-md-8">
                                            <img class="card-img-top pt-3" src={movie.url} alt="Poster"></img>
                                            <div className="card-header">{movie.movieDate}</div>
                                            <div className="card-body">
                                                <h5 className="card-title">{movie.title}</h5>
                                                <p className="card-text">Start: {movie.startTime}</p>
                                                <p className="card-text">End: {movie.endTime}</p>
                                                <p className="card-text">Room: {movie.screeningRoom}</p>
                                                <p className="card-text">Vacant Seats: {movie.vacantSeats}</p>
                                            </div>
                                            <div className="card-footer d-flex justify-content-between">
                                                {
                                                    localStorage.getItem('loginType') == "ROLE_CLIENT" ?
                                                        <Link to={{
                                                            pathname: '/movie-seat',
                                                            state: {
                                                                id: movie.id,
                                                                seats: movie.seats
                                                            }
                                                            }}><button type="button" className="btn btn-danger">Reserve</button></Link>
                                                    :
                                                        ""
                                                }
                                                {
                                                    localStorage.getItem('loginType') == "ROLE_MANAGER" ?
                                                        <Link   to={{
                                                            pathname: '/edit-movie',
                                                            state: {
                                                                id: movie.id,
                                                                title: movie.title,
                                                                date: movie.movieDate,
                                                                startTime: movie.startTime,
                                                                endTime: movie.endTime,
                                                                screeningRoom: movie.screeningRoom
                                                            }
                                                            }}>
                                                            <button type="button" className="btn btn-danger">Edit</button>
                                                        </Link>
                                                    :
                                                        ""
                                                }
                                                {
                                                    (localStorage.getItem('loginType') == "ROLE_MANAGER" || localStorage.getItem('loginType') == "ROLE_GUEST") ?
                                                    <Link to={{
                                                        pathname: '/movie-seat',
                                                        state: {
                                                            id: movie.id,
                                                            seats: movie.seats
                                                        }
                                                        }}>
                                                            <button type="button" className="btn btn-danger">View Seats</button>
                                                        </Link>
                                                    :
                                                        ""
                                                }
                                            </div>
                                        </div>
                                    </>
                                ))
                            :
                                <div className="row justify-content-center" style={{textAlign:"center"}}>
                                    <h2 className="text-white">No movies avaliable to show right now!</h2>
                                </div>
                        }
                    </div>
                </div>
            </div>
        );
    }
}
export default ViewMovies