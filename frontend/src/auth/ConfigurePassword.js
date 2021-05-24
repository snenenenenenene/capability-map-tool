import React, { Component } from "react";
import axios from "axios";
import toast from "react-hot-toast";
import passwordValidator from "password-validator";
import * as sha1 from "js-sha1";

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
    var schema = new passwordValidator();
    schema
      .is()
      .min(8)
      .is()
      .max(100)
      .has()
      .uppercase()
      .has()
      .lowercase()
      .has()
      .digits(2)
      .has()
      .not()
      .spaces();

    if (schema.validate(this.state.password)) {
      var pwd = sha1(this.state.password);
      const formData = new FormData();
      formData.append("username", this.state.username);
      formData.append("email", this.state.email);
      formData.append("roleId", this.state.roleId);
      formData.append("password", pwd);
      formData.append("userId", this.state.userId);
      await axios
        .put(`${process.env.REACT_APP_API_URL}/user/update`, formData)
        .then((response) => {
          toast.success("Successfully Changed Password");
          this.props.handleModal();
          return;
        })
        .catch((error) => {
          toast.error("Failed to Change Password");
          return;
        });
    } else {
      toast.error(
        "Password must be 8 letters, have 1 uppercase letter, 2 numbers"
      );
    }
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
