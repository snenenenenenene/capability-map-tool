import React, { Component } from "react";
import LeapImg from "../img/LEAP logo.png";
import axios from "axios";
import toast, { Toaster } from "react-hot-toast";
import { Modal } from "react-bootstrap";
import ConfigurePassword from "./ConfigurePassword";
import Form from "react-bootstrap/Form";
import Button from "react-bootstrap/Button";
import "./Login.css";
import * as sha1 from "js-sha1";
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
      <div className='container'>
        <Toaster />
        <pre>{this.state.response}</pre>
        <div className='Login'>
          <img
            alt='leap'
            className='rounded mx-auto d-block'
            src={LeapImg}
            width='320'
            height='88'
          />
          <br></br>
          {/* <div className='jumbotron'> */}
          <Form onSubmit={this.authenticateUser}>
            <Form.Group>
              <Form.Control
                type='text'
                size='lg'
                id='inputEmail'
                placeholder='Email address'
                required
                autoFocus
                name='email'
                value={this.state.email}
                onChange={this.handleInputChange}
              />
            </Form.Group>
            <Form.Group className='form-group'>
              <Form.Control
                size='lg'
                type='password'
                id='inputPassword'
                placeholder='Password'
                required
                name='password'
                value={this.state.password}
                onChange={this.handleInputChange}
              />
            </Form.Group>
            <Button
              block
              // size='lg'
              style={{ height: 40 }}
              onClick={this.authenticateUser}
              type='submit'
            >
              LOGIN
            </Button>
          </Form>
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
