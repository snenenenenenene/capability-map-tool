import React, { Component } from "react";
import { Link } from "react-router-dom";
import axios from "axios";
import toast from "react-hot-toast";

export default class EditEnvironment extends Component {
  constructor(props) {
    super(props);
    this.state = {
      environments: [],
      environmentName: "",
      oldEnvironmentName: "",
      environmentId: this.props.match.params.id,
    };
    this.handleSubmit = this.handleSubmit.bind(this);
    this.handleInputChange = this.handleInputChange.bind(this);
  }

  handleSubmit = async (e) => {
    e.preventDefault();
    let jwt = JSON.parse(localStorage.getItem("user")).jwt;

    const formData = new FormData();
    formData.append("environmentName", this.state.environmentName);
    await axios
      .put(
        `${process.env.REACT_APP_API_URL}/environment/${this.state.environmentId}`,
        formData,
        {
          headers: {
            Authorization: `Bearer ${jwt}`,
          },
        }
      )
      .then((response) => toast.success("Updated Environment"))
      .catch((error) => {
        console.log(error);
        toast.error("Failed to Update Environment");
      });
    this.props.history.push(`/home`);
  };

  handleInputChange(event) {
    this.setState({ [event.target.name]: event.target.value });
  }

  async componentDidMount() {
    let jwt = JSON.parse(localStorage.getItem("user")).jwt;

    await axios
      .get(
        `${process.env.REACT_APP_API_URL}/environment/${this.state.environmentId}`,
        {
          headers: {
            Authorization: `Bearer ${jwt}`,
          },
        }
      )
      .then((response) => {
        this.setState({
          environmentName: response.data.environmentName,
          oldEnvironmentName: response.data.environmentName,
        });
      })
      .catch((error) => {
        this.props.history.push("/404");
      });
  }

  render() {
    return (
      <div className='container'>
        <br></br>
        <nav aria-label='breadcrumb'>
          <ol className='breadcrumb'>
            <li className='breadcrumb-item'>
              <Link to={`/`}>Home</Link>
            </li>
            <li className='breadcrumb-item'>{this.state.oldEnvironmentName}</li>
          </ol>
        </nav>
        <div className='jumbotron'>
          <h3>Edit Environment</h3>
          <form onSubmit={this.handleSubmit}>
            <div className='row'>
              <div className='col-sm-6'>
                <div className='form-row'>
                  <div className='form-group col-md-6'>
                    <label htmlFor='EnvironmentName'>Environment Name</label>
                    <input
                      type='text'
                      id='environmentName'
                      name='environmentName'
                      className='form-control'
                      placeholder='Environment Name'
                      value={this.state.environmentName}
                      onChange={this.handleInputChange}
                    />
                  </div>
                </div>
              </div>
              <div className='col-sm-6'></div>
            </div>
            <button
              className='btn btn-primary'
              type='button'
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
