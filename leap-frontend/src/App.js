import React, { Component } from 'react'
import Home from './Components/Home'
import Environment from "./Components/Environment";
import NewEnvironment from "./Components/NewEnvironment";
import UserList from "./Components/UserList"
import RecentEnvironments from './Components/RecentEnvironments'
import AddCapability from './Components/Add/AddCapability'
import './App.css';
import Signup from './auth/Login'
import LeapImg from './img/LEAP logo.png'
import { BrowserRouter, Switch, Route, Link } from 'react-router-dom'


class App extends Component {

  constructor(props) {
    super(props)
    this.state = { authenticated: false }
    this.logout = this.logout.bind(this)
  }

  componentDidMount() {
    if(localStorage.getItem('user')) {
      let user = JSON.parse(localStorage.getItem('user'))
      this.setState({ authenticated: user.authenticated })
    }
  }

  logout() {
    localStorage.removeItem('user')
    window.location.reload()
  }

  render() {
    if(this.state.authenticated === true) {
      return (
          <div>
            <BrowserRouter>
          <nav className='navbar navbar-light navbar-expand' style={{ backgroundColor: 'orangered' }}>
            <a className="navbar-brand" href="">
              <img src={ LeapImg } width="160" height="45"/>
            </a>
            <div className='collapse navbar-collapse' id='navbarNav'>
              <ul className='navbar-nav'>
                <li className='nav-item active'>
                  <Link to={ '/home' } className='nav-link'>Home</Link>
                </li>
                <li className='nav-item active'>
                  <Link to={ '/add' } className='nav-link'>Add</Link>
                </li>
                <li className='nav-item active'>
                  <Link to={ '' } onClick={ this.logout } className='nav-link'>Logout</Link>
                </li>
              </ul>
            </div>
          </nav>
          <div className = 'container'>
              <br/><br/>
              <Switch>
                <Route exact path='/home'><Home/></Route>
                <Route exact path='/environment/:name' component={Environment}/>
                <Route exact path='/environment/:name/add/capability' component={AddCapability}/>
                <Route exact path='/add' component={ NewEnvironment }/>
                <Route exact path='/login' component={ Signup }/>
                <Route exact path='/recent' component={ RecentEnvironments }/>
                <Route exact path='/users' component={ UserList }/>
                <Route path='*'><Home/></Route>
              </Switch>

          </div>
            </BrowserRouter>
          </div>
      )
    }
    else {
      return (
          <BrowserRouter>
            <Route path='*' component={ Signup }/>
          </BrowserRouter>)
    }
  }
}

export default App

