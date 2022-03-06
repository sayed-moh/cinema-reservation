import React, { Component } from "react";
import "./Navbar.css"
import logo from "../../Assets/logo.png"
import { Link } from "react-router-dom";
import axios from 'axios'

class Navbar extends Component {
  constructor() {
    super();
    this.state = {
      loginType: "",
    };
  }

  componentDidMount(){
      if(localStorage.getItem('loginType') != "ROLE_MANAGER" && localStorage.getItem('loginType') != "ROLE_CLIENT" && localStorage.getItem('loginType') != "ROLE_ADMIN"){
        this.setState({loginType: "ROLE_GUEST"})
      }
      else{
        this.setState({loginType: localStorage.getItem('loginType')})
      }
  }

  
  handleLogOut=(e)=>{
    e.preventDefault();
    localStorage.clear();
    localStorage.setItem('loginType', "ROLE_GUEST");
    window.location.replace("/");
  }

  render() {
    return (
          <div id="navbar">
            <div id="navbar-container" className="d-flex justify-content-between">
                <div id="navbar-left">
                    <Link to="/" className="logo mt-2 mb-1" onClick={this.redirectToMain}><img src={logo} alt=""/></Link>
                </div>
              <div id="navbar-right" className="d-flex user-btns">
                <div className="d-flex user-btns-group">
                  {
                  this.state.loginType == "ROLE_MANAGER"?
                  <>
                    <Link className="d-flex flex-column text-center justify-content-center text-white p-3 user-btn market-btn" to="/view-movies">Movies</Link>
                    <Link className="d-flex flex-column text-center justify-content-center text-white p-3 user-btn market-btn" to="/add-movie">Add Movie</Link>
                    <Link className="d-flex flex-column text-center justify-content-center text-white p-3 user-btn market-btn" to="/" onClick={this.handleLogOut}>Logout</Link>
                  </>
                  :
                    this.state.loginType == "ROLE_GUEST"?
                    <>
                      <Link className="d-flex flex-column text-center justify-content-center text-white p-3 user-btn market-btn" to="/view-movies">Movies</Link>
                      <Link className="d-flex flex-column text-center justify-content-center text-white p-3 user-btn market-btn" to="/signup">Signup</Link>
                      <Link className="d-flex flex-column text-center justify-content-center text-white p-3 user-btn market-btn" to="/login">Login</Link>
                    </>
                    :
                      this.state.loginType == "ROLE_ADMIN"?
                        <>
                          <Link className="d-flex flex-column text-center justify-content-center text-white p-3 user-btn market-btn" to="/approve-user">Approve Users</Link>
                          <Link className="d-flex flex-column text-center justify-content-center text-white p-3 user-btn market-btn" to="/delete-user">Delete User</Link>
                          <Link className="d-flex flex-column text-center justify-content-center text-white p-3 user-btn market-btn" to="/" onClick={this.handleLogOut}>Logout</Link>
                        </>
                      :
                      <>
                        <Link className="d-flex flex-column text-center justify-content-center text-white p-3 user-btn market-btn" to="/view-movies">Movies</Link>
                        <Link className="d-flex flex-column text-center justify-content-center text-white p-3 user-btn market-btn" to="/my-reservations">Reservations</Link>
                        <Link className="d-flex flex-column text-center justify-content-center text-white p-3 user-btn market-btn" to="/" onClick={this.handleLogOut}>Logout</Link>
                      </>
                  }
                  
                </div>
              </div>
            </div>
          </div>
    );
      
    }
}

export default Navbar;