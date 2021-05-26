import React, { Component } from "react";
import { Link } from "react-router-dom";
import axios from "axios";
import toast from "react-hot-toast";

export default class EditEnvironment extends Component {
  constructor(props) {
    super(props);
    this.state = {
      environments: [],
      environmentName: this.props.match.params.name,
      environmentId: "",
    };
    this.handleSubmit = this.handleSubmit.bind(this);
    this.handleDateChange = this.handleDateChange.bind(this);
  }

  handleSubmit = async (e) => {
    e.preventDefault();
    const formData = new FormData();
    formData.append("environmentName", this.state.environmentName);
    formData.append("environmentId", this.state.environmentId);
    await axios
      .put(`${process.env.REACT_APP_API_URL}/api/environment/`, formData)
      .then((response) => toast.success("Updated Environment"))
      .catch((error) => {
        console.log(error);
        toast.error("Failed to Update Environment");
      });
    this.props.history.push(`/home`);
  };

  async componentDidMount() {
    await axios
      .get(`${process.env.REACT_APP_API_URL}/api/status/${this.state.statusId}`)
      .then((response) =>
        this.setState({ validityPeriod: response.data.validityPeriod })
      );
  }

  handleDateChange(event) {
    this.setState({ [event.target.name]: event.target.value.toLocaleString() });
    console.log(this.state.validityPeriod);
  }

  render() {
    return (
      <div>
        <br></br>
        <nav aria-label="breadcrumb">
          <ol className="breadcrumb">
            <li className="breadcrumb-item">
              <Link to={`/`}>Home</Link>
            </li>
            <li className="breadcrumb-item">
              <Link to={`/environment/${this.state.environmentName}`}>
                {this.state.environmentName}
              </Link>
            </li>
            <li className="breadcrumb-item">
              <Link to={`/environment/${this.state.environmentName}/status`}>
                Status
              </Link>
            </li>
            <li className="breadcrumb-item">{this.state.statusId}</li>
          </ol>
        </nav>
        <div className="jumbotron">
          <h3>Edit Status</h3>
          <form onSubmit={this.handleSubmit}>
            <div className="row">
              <div className="col-sm-6">
                <div className="form-row">
                  <div className="form-group col-md-6">
                    <label htmlFor="validityPeriod">Validity Period</label>
                    <input
                      type="date"
                      id="validityPeriod"
                      name="validityPeriod"
                      className="form-control"
                      placeholder="End Date"
                      value={this.state.validityPeriod}
                      onChange={this.handleDateChange}
                    />
                  </div>
                </div>
              </div>
              <div className="col-sm-6"></div>
            </div>
            <button
              className="btn btn-primary"
              type="button"
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
