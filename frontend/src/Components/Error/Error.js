import React, { Component } from "react";
import { Link } from "react-router-dom";
import "./Error.css";

export default class Error extends Component {
  constructor(props) {
    super(props);
    this.state = {};
  }

  render() {
    return (
      <div className='container'>
        <div class='row'>
          <div class='col-md-6 align-self-center'></div>
          <div class='col-md-6 align-self-center'>
            <h2>An Error Occurred!</h2>
            <p>
              How you got here is a mystery. But you can click the button below
              to go back to the homepage.
            </p>
            <Link to='/home'>
              <button class='custom-button green'>HOME</button>
            </Link>
          </div>
        </div>
      </div>
    );
  }
}
