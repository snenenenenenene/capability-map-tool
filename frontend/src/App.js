import React, { Component } from "react";
import Home from "./Components/Home";
import Environment from "./Components/Environment";
import NewEnvironment from "./Components/NewEnvironment";
import User from "./Components/User";
import RecentEnvironments from "./Components/RecentEnvironments";
import AddCapability from "./Components/Add/AddCapability";
import "./App.css";
import Signup from "./auth/Login";
import LeapImg from "./img/LEAP logo.png";
import { BrowserRouter, Switch, Route, Link } from "react-router-dom";
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
import Status from "./Components/General/Status";
import NotFoundPage from "./Components/Error/NotFoundPage";
import BusinessProcess from "./Components/General/BusinessProcess";
import EditBusinessProcess from "./Components/Edit/EditBusinessProcess";
import ITApplication from "./Components/General/ITApplication";
import EditITApplication from "./Components/Edit/EditITApplication";
import EditStatus from "./Components/Edit/EditStatus";
import EditProject from "./Components/Edit/EditProject";
import EditStrategy from "./Components/Edit/EditStrategy";
import StrategyItem from "./Components/General/StrategyItem";
import Strategy from "./Components/General/Strategy";
import EditResource from "./Components/Edit/EditResource";
import Resource from "./Components/General/Resource";
import EditStrategyItem from "./Components/Edit/EditStrategyItem";
import EditProgram from "./Components/Edit/EditProgram";
import Program from "./Components/General/Program";
import Admin from "./Components/Admin";
import toast, { Toaster } from "react-hot-toast";
import AddUser from "./Components/AddUser";
import ChoosePassword from "./auth/ConfigurePassword";
import ConfigurePassword from "./auth/ConfigurePassword";
class App extends Component {
  constructor(props) {
    super(props);
    this.state = {
      authenticated: false,
      isOnline: window ? window.navigator.onLine : false,
      roleId: "",
      user: {},
    };
    this.logout = this.logout.bind(this);
  }

  componentDidMount() {
    if (localStorage.getItem("user")) {
      let user = JSON.parse(localStorage.getItem("user"));
      this.setState({ authenticated: user.authenticated });
      this.setState({ user: user });
    }
  }

  logout() {
    localStorage.removeItem("user");
    window.location.reload();
  }

  toastError(object) {
    toast.error(`Could Not Add ${object}`);
  }

  toastSuccess(object) {
    toast.success(`Successfully Added ${object}`);
  }

  adminSettings() {
    console.log(this.state.user);

    if (this.state.user.roleId === 2) {
      return (
        <Link to='/admin'>
          <li className='dropdown-item'>User List</li>
        </Link>
      );
    }
    return;
  }

  render() {
    <BrowserRouter>
          <Route path='/' component={Signup} />
        </BrowserRouter>
  }
}

export default App;
