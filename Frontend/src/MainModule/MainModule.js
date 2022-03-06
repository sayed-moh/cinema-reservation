import React, { Component } from "react";
import {BrowserRouter as Router,Switch,Route,} from "react-router-dom";
import Login from "../Components/Login/Login";
import Signup from "../Components/Signup/Signup";
import ApproveUser from "../Components/Admin/ApproveUser";
import DeleteUser from "../Components/Admin/DeleteUser";
import AddMovie from "../Components/Manager/AddMovie";
import EditMovie from "../Components/Manager/EditMovie";
import ViewMovies from "../Components/Movies/ViewMovies";
import MovieSeats from "../Components/Movies/MovieSeats";
import Main from "../Components/Main/Main";
import ViewReservations from "../Components/Movies/ViewReservations";


class MainModule extends Component {
    render() {
        return(
            <Router>
                <Switch>
                    <Route path="/" component={Main}exact/>
                    <Route path="/login" component={Login}exact/>
                    <Route path="/signup" component={Signup}exact/>
                    <Route path="/approve-user" component={ApproveUser}exact/>
                    <Route path="/delete-user" component={DeleteUser}exact/>
                    <Route path="/add-movie" component={AddMovie}exact/>
                    <Route path="/edit-movie" component={EditMovie}exact/>
                    <Route path="/view-movies" component={ViewMovies}exact/>
                    <Route path="/movie-seat" component={MovieSeats}exact/>
                    <Route path="/my-reservations" component={ViewReservations}exact/>
                </Switch>
            </Router>            
        );
    }
}
export default MainModule;