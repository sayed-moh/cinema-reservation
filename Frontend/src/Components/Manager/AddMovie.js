import { Component } from "react";
import "./Manager.css";
import axios from "axios";
import Navbar from "../Navbar/Navbar";
import {BASEURL} from "../../Constants/BaseUrl";



class AddMovie extends Component{
    constructor(){
        super();
        this.state = {
            fields: {}
        };
        this.handleChange=this.handleChange.bind(this);
        this.test=this.test.bind(this);
    }
    handleChange(field, e) {
        let fields = this.state.fields;
        if(field == "poster"){
            fields[field] = e.target.files[0]
            this.setState({ fields });
            return;
        }
        fields[field] = e.target.value;
        this.setState({ fields });
    }
    test(e){
        e.preventDefault();
        if(this.state.fields["room"] == 1 || this.state.fields["room"] == 2)
        {
            if(this.state.fields["title"] && this.state.fields["date"] && this.state.fields["start-time"] && this.state.fields["end-time"] && this.state.fields["poster"]){
                const formData = new FormData();
                formData.append("poster", this.state.fields["poster"]);
                formData.append('movie', new Blob([JSON.stringify({
                    "title": this.state.fields["title"],
                    "movieDate": this.state.fields["date"],
                    "startTime": this.state.fields["start-time"],
                    "endTime": this.state.fields["end-time"],
                    "screeningRoom": this.state.fields["room"]
                })], {
                        type: "application/json"
                    }));
                axios.post(BASEURL+'/Movies/Movie/Add', formData,
                    {
                        headers: {
                            'Authorization': 'Bearer ' + localStorage.getItem('token'),
                            'Content-Type': 'multipart/form-data'
                        },
                    }
                    )   
                    .then(res => {
                        if(res.status==200){
                            alert("Movie Added Successfully!")
                        }
                    }).catch(err=>{
                        alert(err.response.data)
                    })
            }
            else {
                alert("Complete Movie Details!")
            }
        }
        else {
            alert("Enter Valid Room!")
        }
    }
    render(){
        return(
            <div id="add-movie">
                <Navbar/>
                <div className="container">
                    <h1 className="section-header pt-5 pb-5 text-white">Add New Movie</h1>
                    <form className="form p-5">
                        <div className="form-group p-1">
                        <label className="row text-white">Movie Title: </label>
                            <input type="text" className="form-control col-lg-3 row" placeholder="Title" onChange={this.handleChange.bind(this, "title")}></input>
                        </div>
                        <div className="form-group p-1">
                            <label className="row text-white">Movie Date: </label>
                            <input type="date" className="form-control col-lg-3 row" onChange={this.handleChange.bind(this, "date")}></input>
                        </div>
                        <div className="form-group  p-1">
                            <label className="row text-white">Movie Start Time: </label>
                            <input type="time" className="form-control col-lg-3 row" id="date" onChange={this.handleChange.bind(this, "start-time")}></input>
                        </div>
                        <div className="form-group p-1">
                            <label className="row text-white">Movie End Time: </label>
                            <input type="time" className="form-control col-lg-3 row" onChange={this.handleChange.bind(this, "end-time")}></input>
                        </div>
                        <div className="form-group p-1">
                            <label className="row text-white">Movie Screening Room: </label>
                            <input type="text" className="form-control col-lg-3 row" placeholder="Screening Room" onChange={this.handleChange.bind(this, "room")}></input>
                        </div>
                        <div className="form-group p-1">
                            <label className="row text-white">Movie Poster: </label>
                            <input type="file" className="form-control col-lg-3 row" id="customFile" accept="image/*" onChange={this.handleChange.bind(this, "poster")}/>
                        </div>
                        <div className="row justify-content-center p-3">
                            <button type="submit" className="btn btn-dark"onClick={this.test.bind(this)}>Add</button>
                        </div>
                    </form>
                </div>
            </div>
        );
    }
}
export default AddMovie