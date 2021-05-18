import React, { Component } from 'react'
import * as sha1 from 'js-sha1'
import './Login.css';
import LeapImg from '../img/LEAP logo.png'

export default class Login extends Component {

    constructor(props) {
        super(props)
        this.state =  { username: '', password: '', authenticated: false }
        this.login = this.login.bind(this)
        this.handleInputChange = this.handleInputChange.bind(this)
    }

    login() {
        const pwd = sha1(this.state.password)
        if(this.state.username === 'test'
            && pwd === 'a94a8fe5ccb19ba61c4c0873d391e987982fbbd3') {
            this.setState({ username: this.state.username, password: pwd, authenticated: true })
            localStorage.setItem('user', JSON.stringify({ username: this.state.username, password: pwd, authenticated: true }))
            window.location.reload()
        }
        else {
            // clear user / pwd
            this.setState({ username: '', password: '' })
        }
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
                name='username' value={ this.state.username } onChange={ this.handleInputChange }/>
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
              <button onClick={ this.login } className="btn btn-lg btn-primary btn-block text-uppercase" type="submit">Sign in</button>
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
                            <Input type='text' className='form-control' name='username' value={ this.state.username }
                                   onChange={ this.handleInputChange } placeholder='Enter username' />
                        </FormGroup>
                        <FormGroup row>
                            <Label htmlFor='name'>Password</Label>
                            <Input type='password' className='form-control' name='password' value={ this.state.password }
                                   onChange={ this.handleInputChange } placeholder='Enter password' />
                        </FormGroup>
                    </Col>
                    <Col>
                        <FormGroup row>
                            <button type='button' onClick={ this.login } className='btn btn-dark btn-lg btn-block'>Login</button>
                        </FormGroup>
                    </Col>
                </Form> */
        )
    }
}