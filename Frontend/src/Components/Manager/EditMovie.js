import { Component } from "react";
import Navbar from "../Navbar/Navbar";
import "./Manager.css";
import {BASEURL} from "../../Constants/BaseUrl";
import axios from "axios";



class EditMovie extends Component{
    constructor(){
        super();
        this.state = {
            movieDetails: {},
            oldTitle: ""
        }
        this.test=this.edit.bind(this);
        this.handleChange=this.handleChange.bind(this);
    }
    componentDidMount(){
        let movie = this.props.location.state
        document.getElementById("movie-title").value = movie.title
        document.getElementById("movie-date").value = movie.date
        document.getElementById("movie-start-time").value = movie.startTime
        document.getElementById("movie-end-time").value = movie.endTime
        document.getElementById("movie-screening-room").value = movie.screeningRoom
        this.setState({movieDetails: movie})
        this.setState({oldTitle: movie.title})
    }
    handleChange(field, e) {
        let fields = this.state.movieDetails;
        if(field == "poster"){
            fields[field] = e.target.files[0]
            this.setState({ fields });
            return;
        }
        fields[field] = e.target.value;
        this.setState({ fields });
    }
    edit(e){
        e.preventDefault();
        const formData = new FormData();
        if(this.state.movieDetails["screeningRoom"] == 1 || this.state.movieDetails["screeningRoom"] == 2){
            if(this.state.movieDetails["poster"]){
                formData.append("poster", this.state.movieDetails["poster"]);
            }
            formData.append('movie', new Blob([JSON.stringify({
                "title": this.state.movieDetails["title"],
                "movieDate": this.state.movieDetails["date"],
                "startTime": this.state.movieDetails["startTime"],
                "endTime": this.state.movieDetails["endTime"],
                "screeningRoom": this.state.movieDetails["screeningRoom"],
                "id": this.state.movieDetails["id"]
            })], {
                    type: "application/json"
                }));
            axios.put(BASEURL+'/Movies/Movie/Update', formData,
                {
                    headers: {
                        'Authorization': 'Bearer ' + localStorage.getItem('token'),
                        'Content-Type': 'multipart/form-data'
                    },
                }
                )   
                .then(res => {
                    if(res.status==200){
                        alert("Movie Updated Successfully!")
                    }
                }).catch(err=>{
                    alert(err.response.data)
                })
        }
        else {
            alert("Enter Valid Room!")
        }
    }
    render(){
        return(
            <div id="edit-movie">
                <Navbar/>
                <div className="container">
                    <h1 className="section-header pt-5 pb-5 text-white">Edit <u>{this.state.oldTitle}</u> Movie</h1>
                    <form className="form p-5">
                        <div className="form-group p-1">
                        <label className="row text-white">Movie Title: </label>
                            <input type="text" id="movie-title" className="form-control col-lg-3 row" placeholder="Title" onChange={this.handleChange.bind(this, "title")}></input>
                        </div>
                        <div className="form-group p-1">
                            <label className="row text-white">Movie Date: </label>
                            <input type="date" id="movie-date" className="form-control col-lg-3 row" onChange={this.handleChange.bind(this, "date")}></input>
                        </div>
                        <div className="form-group  p-1">
                            <label className="row text-white">Movie Start Time: </label>
                            <input type="time" id="movie-start-time" className="form-control col-lg-3 row" onChange={this.handleChange.bind(this, "startTime")}></input>
                        </div>
                        <div className="form-group p-1">
                            <label className="row text-white">Movie End Time: </label>
                            <input type="time" id="movie-end-time" className="form-control col-lg-3 row" onChange={this.handleChange.bind(this, "endTime")}></input>
                        </div>
                        <div className="form-group p-1">
                            <label className="row text-white">Movie Screening Room: </label>
                            <input type="text" id="movie-screening-room" className="form-control col-lg-3 row" placeholder="Screening Room" onChange={this.handleChange.bind(this, "screeningRoom")}></input>
                        </div>
                        <div className="form-group p-1">
                            <label className="row text-white">Movie Poster: </label>
                            <input type="file" className="form-control col-lg-3 row" id="customFile" accept="image/*" onChange={this.handleChange.bind(this, "poster")}/>
                        </div>
                        <div className="row justify-content-center p-3">
                            <button type="submit" className="btn btn-dark" onClick={this.edit.bind(this)}>Edit</button>
                        </div>
                    </form>
                </div>
            </div>
        );
    }
}
export default EditMovie