import React, { Component } from 'react'
import Home from './Components/Home'
import Environment from "./Components/Environment";
import NewEnvironment from "./Components/NewEnvironment";
import User from "./Components/User"
import RecentEnvironments from './Components/RecentEnvironments'
import AddCapability from './Components/Add/AddCapability'
import './App.css';
import Signup from './auth/Login'
import LeapImg from './img/LEAP logo.png'
import { BrowserRouter, Switch, Route, Link } from 'react-router-dom'
import AddResource from "./Components/Add/AddResource";
import AddStrategy from "./Components/Add/AddStrategy";
import AddBusinessProcess from "./Components/Add/AddBusinessProcess";
import AddITApplication from "./Components/Add/AddITApplication";
import AddProgram from "./Components/Add/AddProgram";
import AddStatus from "./Components/Add/AddStatus";
import AddStrategyItem from "./Components/Add/AddStrategyItem";
import AddProject from "./Components/Add/AddProject";
import EditCapability from "./Components/Edit/EditCapability";
import Capability from "./Components/General/Capability";
import Project from "./Components/General/Project";
import NotFoundError from "./Components/Error/NotFound";
import GeneralError from "./Components/Error/Error";
import Status from './Components/General/Status';
import NotFoundPage from "./Components/Error/NotFoundPage"
import BusinessProcess from './Components/General/BusinessProcess';
import EditBusinessProcess from './Components/Edit/EditBusinessProcess';
import ITApplication from './Components/General/ITApplication';
import EditITApplication from './Components/Edit/EditITApplication';
import EditStatus from './Components/Edit/EditStatus';
import EditProject from './Components/Edit/EditProject';
import EditStrategy from './Components/Edit/EditStrategy';
import StrategyItem from './Components/General/StrategyItem';
import Strategy from './Components/General/Strategy';
import EditResource from './Components/Edit/EditResource';
import Resource from './Components/General/Resource';
import EditStrategyItem from './Components/Edit/EditStrategyItem';
import EditProgram from './Components/Edit/EditProgram';
import Program from './Components/General/Program';
import Admin from './Components/Admin';
import toast, { Toaster } from 'react-hot-toast';
import AddUser from './Components/AddUser';
import ChoosePassword from './auth/ChoosePassword';
class App extends Component {

  constructor(props) {
    super(props)
    this.state = { 
      authenticated: false,
      isOnline: window ? window.navigator.onLine : false
    }
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

  toastError(object) {
    toast.error(`Could Not Add ${object}`)
  }
  
  toastSuccess(object) {
    toast.success(`Sucessfully Added ${object}`)
  }

  render() {
    if(this.state.authenticated === true) {
      return (
          <div className="bg_image">
            <Toaster/>
            <BrowserRouter>
              <nav className="navbar navbar-expand-lg navbar-dark sticky-top" style={{ backgroundColor: '#ff754f'}}>
                <button className="navbar-toggler" type="button" data-toggle="collapse"
                        data-target="#navbarTogglerDemo01" aria-controls="navbarTogglerDemo01" aria-expanded="false"
                        aria-label="Toggle navigation">
                  <span className="navbar-toggler-icon"></span>
                </button>
                <div className="collapse navbar-collapse" id="navbarTogglerDemo01">
                  <Link to={ '/home' } className='navbar-brand'><img alt="leap" src={ LeapImg } width="80" height="22"/></Link>
                  <ul className="navbar-nav mr-auto mt-2 mt-lg-0">
                  </ul>
                  <form className="form-inline my-2 my-lg-0">
                    <Link to={ '' } onClick={ this.logout } style={{ color: '#fff'}} className='nav-link'>Logout</Link>
                    <div class="collapse navbar-collapse" id="navbarNavDarkDropdown">
                  <ul class="navbar-nav">
                    <li class="nav-item dropdown">
                    <i className="bi bi-gear-fill" href="#" id="navbarDarkDropdownMenuLink" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                    </i>
                      <ul class="dropdown-menu dropdown-menu-dark" aria-labelledby="navbarDarkDropdownMenuLink">
                        <Link to="/settings"><li className="dropdown-item">Settings</li></Link>
                        <Link to="/admin"><li className="dropdown-item">Admin</li></Link>
                      </ul>
                    </li>
                  </ul>
                </div>
                  </form>
                </div>
              </nav>
          <div className = 'container'>
              <Switch>
              {/* ROOT */}
              <Route exact path='/home' component={Home}/>
              <Route exact path='/' component={Home}/>
              {/* ENVIRONMENTS */}
              <Route exact path='/add' component={ NewEnvironment }/>
              <Route exact path='/environment/:name' component={Environment}/>
              <Route exact path='/recent' component={ RecentEnvironments }/>
              {/* CAPABILITIES */}
              <Route exact path='/environment/:name/capability/add' component={AddCapability}/>
              <Route exact path='/environment/:name/capability/:id' component={EditCapability}/>
              <Route exact path='/environment/:name/capability' component={Capability}/>
              {/* STRATEGIES */}
              <Route exact path='/environment/:name/strategy/add' component={AddStrategy}/>
              <Route exact path='/environment/:name/strategy/:id' component={EditStrategy}/>
              <Route exact path='/environment/:name/strategy' component={Strategy}/>

              {/* RESOURCES */}
              <Route exact path='/environment/:name/resource/add' component={AddResource}/>
              <Route exact path='/environment/:name/resource/:id' component={EditResource}/>
              <Route exact path='/environment/:name/resource' component={Resource}/>

              {/* ITAPPLICATIONS */}
              <Route exact path='/environment/:name/itapplication/add' component={AddITApplication}/>
              <Route exact path='/environment/:name/itapplication/:id' component={EditITApplication}/>
              <Route exact path='/environment/:name/itapplication' component={ITApplication}/>
              {/* BUSSINESSPROCESSES */}
              <Route exact path='/environment/:name/businessprocess/add' component={AddBusinessProcess}/>
              <Route exact path='/environment/:name/businessprocess/:id' component={EditBusinessProcess}/>
              <Route exact path='/environment/:name/businessprocess' component={BusinessProcess}/>
              {/* STATUSES */}
              <Route exact path='/environment/:name/status/add' component={AddStatus}/>
              <Route exact path='/environment/:name/status/:id' component={EditStatus}/>
              <Route exact path='/environment/:name/status' component={Status}/>
              {/* PROJECTS */}
              <Route exact path='/environment/:name/project/add' component={AddProject}/>
              <Route exact path='/environment/:name/project/:id' component={EditProject}/>
              <Route exact path='/environment/:name/project' component={Project}/>
              {/* STRATEGYITEMS */}
              <Route exact path='/environment/:name/strategyitem/add' component={AddStrategyItem}/>
              <Route exact path='/environment/:name/strategyitem/:id' component={EditStrategyItem}/>
              <Route exact path='/environment/:name/strategyitem' component={StrategyItem}/>

              {/* PROGRAMS */}
              <Route exact path='/environment/:name/program/add' component={AddProgram}/>
              <Route exact path='/environment/:name/program/:id' component={EditProgram}/>
              <Route exact path='/environment/:name/program' component={Program}/>

              {/* USERS */}
              <Route exact path='/login' component={ Signup }/>
              <Route exact path='/user' component={ User }/>
              <Route exact path='/user/add' component={ AddUser }/>
              <Route exact path='/choosePassword' component={ ChoosePassword }/>
              <Route exact path='/admin' component={ Admin }/>              
              {/* ERRORS */}
              <Route exact path='/error' component={ GeneralError }/>
              <Route exact path='/notfound' component={ NotFoundError }/>
              <Route component={NotFoundPage} />
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

