import React, { Component } from "react";
import LeapImg from "../../img/LEAP_black.png";
import axios from "axios";
import toast, { Toaster } from "react-hot-toast";
import { Modal } from "react-bootstrap";
import ConfigurePassword from "./ConfigurePassword";
import "./Login.css";
import * as sha1 from "js-sha1";
import "bootstrap/dist/css/bootstrap.min.css";

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
    let formData = new FormData();
    formData.append("email", this.state.email);
    formData.append("password", this.state.password);
    axios
      .post(`${process.env.REACT_APP_API_URL}/user/authenticate`, formData)
      .then((response) => {
        console.log(response.data);
        if (response.data === "Bad credentials") {
          return toast.error("Bad Credentials");
        }
        toast.success(`Successful Login! \n Welcome ${this.state.username}`);
        this.setState({ authenticated: true });
        localStorage.setItem(
          "user",
          JSON.stringify({
            email: this.state.email,
            userId: response.data.userId,
            authenticated: true,
            jwt: response.data,
          })
        );
        this.props.history.push(`/home`);
        window.location.reload();
        return;
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
      <div
        className='container'
        style={{
          marginTop: 15 + "%",
          marginBottom: 20 + "%",
        }}
      >
        <Toaster />
        <div className='text-center'>
          <img
            alt='leap'
            className='mx-auto d-block logo'
            src={LeapImg}
            width='320'
            height='88'
          />
          <br></br>
          <form onSubmit={this.authenticateUser}>
            <div className='form-group col-sm-5 mx-auto'>
              <input
                type='text'
                id='inputEmail'
                placeholder='EMAIL (super_admin)'
                required
                autoFocus
                name='email'
                className='login-input text-center'
                value={this.state.email}
                onChange={this.handleInputChange}
              />
            </div>
            <div className='form-group col-sm-5 mx-auto'>
              <input
                size='lg'
                type='password'
                id='inputPassword'
                placeholder='PASSWORD (super_admin)'
                required
                name='password'
                className='login-input text-center'
                value={this.state.password}
                onChange={this.handleInputChange}
              />
            </div>
            <button
              style={{ height: 40 }}
              className='custom-button green'
              onClick={this.authenticateUser}
              type='submit'
            >
              LOGIN
            </button>
            <p>@Bavo&amp;Stepbros</p>
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
              Close
            </button>
          </Modal.Footer>
        </Modal>
      </div>
    );
  }
}
