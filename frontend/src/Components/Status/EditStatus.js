import React, { Component } from "react";
import { Link } from "react-router-dom";
import axios from "axios";
import toast from "react-hot-toast";

export default class EditStatus extends Component {
  constructor(props) {
    super(props);
    this.state = {
      status: {},
      environments: [],
      environmentName: this.props.match.params.name,
      statusId: this.props.match.params.id,
      validityPeriod: new Date(),
    };
    this.handleSubmit = this.handleSubmit.bind(this);
    this.handleDateChange = this.handleDateChange.bind(this);
  }

  handleSubmit = async (e) => {
    e.preventDefault();
    let jwt = JSON.parse(localStorage.getItem("user")).jwt;
    const formData = new FormData();
    formData.append("validityPeriod", this.state.validityPeriod);
    formData.append("statusId", this.state.statusId);
    await axios
      .put(`${process.env.REACT_APP_API_URL}/api/status/`, formData, {
        headers: {
          Authorization: `Bearer ${jwt}`,
        },
      })
      .then((response) => toast.success("Updated Status"))
      .catch((error) => {
        toast.error("Failed to Update Status");
      });
    this.props.history.push(
      `/environment/${this.state.environmentName}/status`
    );
  };

  async componentDidMount() {
    let jwt = JSON.parse(localStorage.getItem("user")).jwt;

    await axios
      .get(
        `${process.env.REACT_APP_API_URL}/environment/environmentname/${this.state.environmentName}`,
        {
          headers: {
            Authorization: `Bearer ${jwt}`,
          },
        }
      )
      .then((response) =>
        this.setState({ environmentId: response.data.environmentId })
      )
      .catch((error) => {
        console.log(error);
        this.props.history.push("/404");
      });

    await axios
      .get(
        `${process.env.REACT_APP_API_URL}/api/status/${this.state.statusId}`,
        {
          headers: {
            Authorization: `Bearer ${jwt}`,
          },
        }
      )
      .then((response) =>
        this.setState({ validityPeriod: response.data.validityPeriod })
      );
  }

  handleDateChange(event) {
    this.setState({ [event.target.name]: event.target.value.toLocaleString() });
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
            <li className='breadcrumb-item'>
              <Link to={`/environment/${this.state.environmentName}`}>
                {this.state.environmentName}
              </Link>
            </li>
            <li className='breadcrumb-item'>
              <Link to={`/environment/${this.state.environmentName}/status`}>
                Status
              </Link>
            </li>
            <li className='breadcrumb-item'>{this.state.statusId}</li>
          </ol>
        </nav>
        <div className='jumbotron'>
          <h3>Edit Status</h3>
          <form onSubmit={this.handleSubmit}>
            <div className='row'>
              <div className='col-sm-6'>
                <div className='form-row'>
                  <div className='form-group col-md-6'>
                    <label htmlFor='validityPeriod'>Validity Period</label>
                    <input
                      type='date'
                      id='validityPeriod'
                      name='validityPeriod'
                      className='form-control'
                      placeholder='End Date'
                      value={this.state.validityPeriod}
                      onChange={this.handleDateChange}
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
