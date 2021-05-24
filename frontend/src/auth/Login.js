import React, { Component } from "react";
import LeapImg from "../img/LEAP logo.png";
import axios from "axios";
import toast, { Toaster } from "react-hot-toast";
import { Modal } from "react-bootstrap";
import ConfigurePassword from "./ConfigurePassword";
import "./Login.css";
import * as sha1 from "js-sha1";
import 'bootstrap/dist/css/bootstrap.min.css';
// import * as bcrypt from "bcrypt";

export default class Login extends Component {
  constructor(props) {
    super(props);
    this.state = {
      showModal: false,
      username: "",
      email: "",
      password: "",
      roleId: "",
      userId: "",
      authenticated: false,
    };
    this.authenticateUser = this.authenticateUser.bind(this);
    this.handleInputChange = this.handleInputChange.bind(this);
    this.handleModal = this.handleModal.bind(this);
  }

  handleModal() {
    this.setState({ showModal: !this.state.showModal });
  }

  authenticateUser = async (e) => {
    e.preventDefault();
    if(this.state.password === "preview" && this.state.email === "preview"){
      toast.success(`Successful Login! \n Welcome ${this.state.username}`);
      this.setState({ authenticated: true });
      localStorage.setItem(
        "user",
        JSON.stringify({
          email: this.state.email,
          userId: 12345,
          authenticated: true,
          roleId: 1,
          username: "Preview User",
        })
      );
      this.props.history.push(`/home`);
      window.location.reload();
    }
    const pwd = sha1(this.state.password);
    let formData = new FormData();
    formData.append("email", this.state.email);
    axios
      .post(`${process.env.REACT_APP_API_URL}/user/authenticate`, formData)
      .then((response) => {
        if (sha1("newUser") === response.data.password) {
          toast.success(`Welcome ${this.state.email} Let's Pick a Password!`);
          this.handleModal();
          return;
        }
        if (pwd === response.data.password) {
          toast.success(`Successful Login! \n Welcome ${this.state.username}`);
          this.setState({ authenticated: true });
          localStorage.setItem(
            "user",
            JSON.stringify({
              email: this.state.email,
              userId: response.data.userId,
              authenticated: true,
              roleId: response.data.roleId,
              username: response.data.username,
            })
          );
          this.props.history.push(`/home`);
          window.location.reload();
          return;
        } else if (pwd !== response.data.password) {
          toast.error("Wrong password!");
          return;
        }
        toast.error("Something went Wrong");
      })
      .catch((error) => {
        if (error.response) {
          if (error.response.status === 400) {
            toast.error("Wrong Email Address");
            return;
          }
        }
        toast.error("Something went Wrong");
      });
  };

  handleInputChange(event) {
    this.setState({ [event.target.name]: event.target.value });
  }

  render() {
    return (
      <div className='container text-center jumbotron linear-jumbotron shadow-lg' style={{marginBottom: 1 + "%", marginTop: 1.5 + "%",height : 95 + "%", width: 100 + "%"}} >
        <Toaster />
        <div className='Login text-center'>
          <img
            alt='leap'
            className='rounded mx-auto d-block'
            src={LeapImg}
            width='320'
            height='88'
          />
          <br></br>
          {/* <div className='jumbotron'> */}
          <form onSubmit={this.authenticateUser}>
            <div className="form-group">
              <input
                type='text'
                id='inputEmail'
                placeholder='Email address (admin)'
                required
                autoFocus
                name='email'
                className="form-control form-control-lg text-center"
                value={this.state.email}
                onChange={this.handleInputChange}
              />
            </div>
            <div className='form-group'>
              <input
                size='lg'
                type='password'
                id='inputPassword'
                placeholder='Password (admin)'
                required
                name='password'
                className="form-control form-control-lg text-center"
                value={this.state.password}
                onChange={this.handleInputChange}
              />
            </div>
            <button
              style={{ height: 40 }}
              className="btn btn-primary"
              onClick={this.authenticateUser}
              type='submit'
            >
              LOGIN
            </button>
          </form>
          {/* </div> */}
        </div>
        <Modal show={this.state.showModal}>
          <Modal.Header>Configure Password</Modal.Header>
          <Modal.Body>
            <ConfigurePassword
              handleModal={this.handleModal}
              userId={this.state.userId}
              email={this.state.email}
              username={this.state.username}
              roleId={this.state.roleId}
              fetchConfigurePassword={this.fetchConfigurePassword}
            />
          </Modal.Body>
          <Modal.Footer>
            <button
              type='button'
              className='btn btn-secondary'
              onClick={() => this.handleModal()}
            >
              Close Modal
            </button>
          </Modal.Footer>
        </Modal>
      </div>
    );
  }
}
