import axios from "axios";
import React, { Component } from "react";
import { Link } from "react-router-dom";
import toast from "react-hot-toast";
import * as sha1 from "js-sha1";

export default class Settings extends Component {
  constructor(props) {
    super(props);
    this.state = {
      environments: {},
      user: JSON.parse(localStorage.getItem("user")),
      password: "",
    };
    this.handleSubmit = this.handleSubmit.bind(this);
    this.handleInputChange = this.handleInputChange.bind(this);
  }

  async componentDidMount() {
    await axios
      .get(`${process.env.REACT_APP_API_URL}/user/${this.state.user.userId}`)
      .then((response) =>
        this.setState({
          username: response.data.username,
          email: response.data.email,
          roleId: response.data.roleId,
        })
      )
      .catch((error) => {
        toast.error("Could not Load User");
      });
  }

  handleInputChange(event) {
    this.setState({ [event.target.name]: event.target.value });
  }

  handleSubmit = async (e) => {
    e.preventDefault();
    const formData = new FormData();
    formData.append("userId", this.state.user.userId);
    formData.append("username", this.state.username);
    formData.append("email", this.state.user.email);
    formData.append("password", sha1(this.state.password));
    formData.append("roleId", this.state.user.roleId);
    await axios
      .put(`${process.env.REACT_APP_API_URL}/user/`, formData)
      .then((response) => {
        toast.success("User Updated Successfully!");
        this.props.history.push(`/user`);
      })
      .catch((error) => toast.error("Could not Update User"));
  };

  render() {
    return (
      <div className='container'>
        <br></br>
        <nav aria-label='breadcrumb'>
          <ol className='breadcrumb'>
            <li className='breadcrumb-item'>
              <Link to={`/home`}>Home</Link>
            </li>
            <li className='breadcrumb-item'>Settings</li>
          </ol>
        </nav>
        <div className='jumbotron'>
          <h3>User Settings</h3>
          <form onSubmit={this.handleSubmit}>
            <div className='form-group'>
              <label htmlFor='email' className='sr-only'>
                Email
              </label>
              <input
                type='text'
                id='email'
                name='email'
                className='form-control-plaintext'
                placeholder='email'
                readonly
                value={this.state.email}
              />
            </div>
            <div className='form-row'>
              <div className='form-group col-md-6'>
                <label htmlFor='username'>Username</label>
                <input
                  type='text'
                  id='username'
                  name='username'
                  className='form-control'
                  placeholder='Username'
                  value={this.state.username}
                  onChange={this.handleInputChange}
                  required
                />
              </div>
            </div>
            <div className='form-row'>
              <div className='form-group col-md-6'>
                <label htmlFor='password'>Password</label>
                <input
                  type='password'
                  id='password'
                  name='password'
                  className='form-control'
                  placeholder='Password'
                  value={this.state.password}
                  onChange={this.handleInputChange}
                  required
                />
              </div>
            </div>
            <button
              className='btn btn-primary'
              type='submit'
              onClick={this.handleSubmit}
            >
              Submit
            </button>
          </form>
        </div>
      </div>
    );
  }
}
