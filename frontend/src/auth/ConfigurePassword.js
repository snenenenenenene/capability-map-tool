import React, { Component } from 'react'
import './Login.css';
import LeapImg from '../img/LEAP logo.png'
import axios from 'axios';
import toast,{Toaster} from 'react-hot-toast';

export default class ConfigurePassword extends Component {
  constructor(props) {
    super(props);
    this.state = {
      email: '',
      password: '',
      confirmNewPassword: ''
    };
    this.handleSubmit = this.handleSubmit.bind(this);
    this.handleInputChange = this.handleInputChange.bind(this)
}

handleSubmit = async e => {
  e.preventDefault();
}

handleInputChange(event) {
  this.setState({ [event.target.name]: event.target.value })
}

fetchConfigurePassword(){

}

    render() {
        return (
          <div>
            <form onSubmit={ this.authenticateUser }className="form-signin">
              <div className="form-label-group">
              <label htmlFor="password">New Password</label>
                <input type="password" id="password" className="form-control" placeholder="New Password" required autoFocus 
                name='password' value={ this.state.password } onChange={ this.handleInputChange }/>
              </div>
              <div className="form-label-group">
              <label htmlFor="confirmNewPassword">Confirm Password</label>
                <input type="password" id="confirmNewPassword" className="form-control" placeholder="Confirm Password" required 
                name='confirmNewPassword' value={ this.state.confirmNewPassword } onChange={ this.handleInputChange }/>
              </div>
              <br></br>
              <button onClick={ this.authenticateUser } className="btn btn-lg btn-primary btn-block text-uppercase" type="submit">Configure Password</button>
            </form>
        </div>
        )
    }
}
