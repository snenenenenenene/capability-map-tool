import React, { Component } from 'react'
import * as sha1 from 'js-sha1'
import './Login.css';
import LeapImg from '../img/LEAP logo.png'
import axios from 'axios';
import toast, { Toaster } from 'react-hot-toast';

export default class ChoosePassword extends Component {

    constructor(props) {
        super(props)
        this.state =  { 
          email: '',
          password: '',
          authenticated: false }
        this.authenticateUser = this.authenticateUser.bind(this)
        this.handleInputChange = this.handleInputChange.bind(this)
    }

    authenticateUser() {
      console.log(this.state.email)
      let formData = new FormData()
      formData.append("email", this.state.email)
      axios.post(`${process.env.REACT_APP_API_URL}/user/authenticate`, formData)
      .then(response => {
        if (response.password === null){
          
        }

        console.log(response.data)
        // this.setState({ email: this.state.email, password: pwd, authenticated: true })
        // localStorage.setItem('user', JSON.stringify({ email: this.state.email, password: pwd, authenticated: true }))
      }
      ).catch(error => toast.error("Auth Servers are Down"))
    }

    handleInputChange(event) {
        this.setState({ [event.target.name]: event.target.value })
    }

    render() {
        return (
            <div className="container">
                <div className="row">
      <div className="col-sm-9 col-md-7 col-lg-5 mx-auto">
          <br></br>
      <img alt="leap" className="rounded mx-auto d-block" src={ LeapImg } width="320" height="88"/>
        <div className="card card-signin my-5">
          <div className="card-body">
            <h5 className="card-title text-center">Sign In</h5>
            <form className="form-signin">
              <div className="form-label-group">
              <label htmlFor="inputEmail">Email address</label>
                <input type="text" id="inputEmail" className="form-control" placeholder="Email address" required autoFocus 
                name='email' value={ this.state.email } onChange={ this.handleInputChange }/>
              </div>
              <div className="form-label-group">
              <label htmlFor="inputPassword">Password</label>
                <input type="password" id="inputPassword" className="form-control" placeholder="Password" required 
                name='password' value={ this.state.password } onChange={ this.handleInputChange }/>
              </div>
              <div className="custom-control custom-checkbox mb-3">
                <input type="checkbox" className="custom-control-input" id="customCheck1"/>
                <label className="custom-control-label" htmlFor="customCheck1">Remember password</label>
              </div>
              <button onClick={ this.authenticateUser } className="btn btn-lg btn-primary btn-block text-uppercase" type="submit">Sign in</button>
            </form>
          </div>
        </div>
      </div>
    </div>
    </div>
                /* <h1 className='display-4'>Login</h1>
                <br/>
                <Form className='form-group w-50'>
                    <Col>
                        <FormGroup row>
                            <Label htmlFor='name'>Name</Label>
                            <Input type='text' className='form-control' name='email' value={ this.state.email }
                                   onChange={ this.handleInputChange } placeholder='Enter email' />
                        </FormGroup>
                        <FormGroup row>
                            <Label htmlFor='name'>Password</Label>
                            <Input type='password' className='form-control' name='password' value={ this.state.password }
                                   onChange={ this.handleInputChange } placeholder='Enter password' />
                        </FormGroup>
                    </Col>
                    <Col>
                        <FormGroup row>
                            <button type='button' onClick={ this.authenticateUser } className='btn btn-dark btn-lg btn-block'>Login</button>
                        </FormGroup>
                    </Col>
                </Form> */
        )
    }
}