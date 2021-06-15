import React, { Component } from "react";
import { Link } from "react-router-dom";
import toast from "react-hot-toast";
import LeapImg from "../../img/LEAP_black.png";
import API from "../../Services/API";

export default class Settings extends Component {
  constructor(props) {
    super(props);
    this.state = {
      api: new API(),
      environments: {},
      email: "",
    };
    this.sendMail = this.sendMail.bind(this);
    this.handleInputChange = this.handleInputChange.bind(this);
  }

  handleInputChange(event) {
    this.setState({ [event.target.name]: event.target.value });
  }

  sendMail = async (e) => {
    this.state.api.createEntity({ name: "user" });
    e.preventDefault();
    const formData = new FormData();
    formData.append("email", this.state.user.email);
    await this.state.api.endpoints.user
      .forgotPassword(formData)
      .then((response) => {
        toast.success(`Mail Sent to ${this.state.email}`);
        this.props.history.push(`/login`);
      })
      .catch((error) => toast.error("Something went Wrong"));
  };

  render() {
    return (
      <div
        className="container"
        style={{
          marginTop: 15 + "%",
          marginBottom: 20 + "%",
        }}
      >
        {/* <Toaster /> */}
        <div className="text-center">
          <h1
            alt="leap"
            className="mx-auto d-block top-text"
            src={LeapImg}
            width="320"
            height="88"
          >
            FORGOT PASSWORD
          </h1>
          <br></br>
          <form onSubmit={this.sendMail}>
            <div className="form-group col-sm-5 mx-auto">
              <input
                type="text"
                id="inputEmail"
                placeholder="EMAIL"
                required
                autoFocus
                name="email"
                className="login-input text-center"
                value={this.state.email}
                onChange={this.handleInputChange}
              />
            </div>
            <button
              style={{ height: 40 }}
              className="custom-button green"
              onClick={this.sendMail}
              type="submit"
            >
              SEND MAIL
            </button>
          </form>
          <Link className="bottom-text" to="/login">
            <p>Back</p>
          </Link>
        </div>
      </div>
    );
  }
}
