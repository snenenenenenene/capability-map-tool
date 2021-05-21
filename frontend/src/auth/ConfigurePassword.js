import React, { Component } from "react";
import axios from "axios";
import toast from "react-hot-toast";

export default class ConfigurePassword extends Component {
  constructor(props) {
    super(props);
    this.state = {
      username: props.username,
      email: props.email,
      roleId: props.roleId,
      password: "",
      userId: props.userId,
      confirmNewPassword: "",
    };

    this.handleSubmit = this.handleSubmit.bind(this);
    this.handleInputChange = this.handleInputChange.bind(this);
  }

  handleSubmit = async (e) => {
    e.preventDefault();
    const formData = new FormData();
    formData.append("username", this.state.username);
    formData.append("email", this.state.email);
    formData.append("roleId", this.state.roleId);
    formData.append("password", this.state.password);
    formData.append("userId", this.state.userId);

    await axios
      .put(`http://localhost:8080/api/user/update`, formData)
      .then((response) => {
        toast.success("Successfully Changed Password");
        this.props.handleModal();
        return;
      })
      .catch((error) => {
        toast.error("Failed to Change Password");
        return;
      });
  };

  handleInputChange(event) {
    this.setState({ [event.target.name]: event.target.value });
  }

  fetchConfigurePassword() {}
  render() {
    return (
      <div>
        <form onSubmit={this.authenticateUser} className='form-signin'>
          <div className='form-label-group'>
            <label htmlFor='password'>New Password</label>
            <input
              type='password'
              id='password'
              className='form-control'
              placeholder='New Password'
              required
              autoFocus
              name='password'
              value={this.state.password}
              onChange={this.handleInputChange}
            />
          </div>
          <div className='form-label-group'>
            <label htmlFor='confirmNewPassword'>Confirm Password</label>
            <input
              type='password'
              id='confirmNewPassword'
              className='form-control'
              placeholder='Confirm Password'
              required
              name='confirmNewPassword'
              value={this.state.confirmNewPassword}
              onChange={this.handleInputChange}
            />
          </div>
          <br></br>
          <button
            onClick={this.handleSubmit}
            className='btn btn-lg btn-primary btn-block text-uppercase'
            type='submit'
          >
            Configure Password
          </button>
        </form>
      </div>
    );
  }
}
