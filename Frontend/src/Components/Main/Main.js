import React, { Component } from "react";
import "./Main.css"
import AOS from 'aos';
import 'aos/dist/aos.css'; 
import Ticket1 from "../../Assets/ticket1.png"
import Ticket2 from "../../Assets/ticket2.png"
import Navbar from "../Navbar/Navbar";


class Main extends Component {
    componentDidMount() {
        AOS.init({
            duration : 3000,
            once: false,
            mirror: true
        });
      }


render() {
    return (
        <div id="home-page">
            <Navbar/>
            <div className="quote-box quote-box-2 row">
                <div className="quote-2 col" data-aos={"fade-right"}>
                    <h1 className="quote-2-itself">"Cinema is not only about making people dream. It's about changing things and making people think."</h1>
                    <h1 className="quote-speaker">-Nadine Labaki</h1>
                </div>
                <img id="image" src={Ticket1} alt="" data-aos={"fade-left"} className="col quote-2-img aos-item"></img>
            </div>
            <div className="quote-box quote-box-1 row">
                <img id="image" src={Ticket2} alt="" data-aos={"fade-right"} className="col quote-1-img aos-item"></img>
                <div className="quote-1 col" data-aos={"fade-left"}>
                    <h1 className="quote-1-itself">"Cinema is a mirror by which we often see ourselves."</h1>
                    <h1 className="quote-speaker">-Alejandro Gonzalez Inarritu</h1>
                </div>
            </div>
        </div>    
    );
  }
}

export default Main;
