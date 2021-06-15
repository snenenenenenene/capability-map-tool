import React, { Component } from "react";
import { Link } from "react-router-dom";
import toast from "react-hot-toast";
import API from "../../Services/API";

export default class AddInfo extends Component {
  constructor(props) {
    super(props);
    this.state = {
      api: new API(),
      statuses: [],
      programs: [],
      environmentName: this.props.match.params.name,
      environmentId: "",
      informationName: "",
      informationDescription: "",
    };
    this.handleSubmit = this.handleSubmit.bind(this);
    this.handleInputChange = this.handleInputChange.bind(this);
  }
  //HANDLE SUBMIT
  handleSubmit = async (e) => {
    e.preventDefault();
    const formData = new FormData();
    formData.append("informationName", this.state.informationName);
    formData.append(
      "informationDescription",
      this.state.informationDescription
    );
    await this.state.api.endpoints.information
      .create(formData)
      .then((response) => {
        toast.success("Info Added Successfully!");
        this.setState({ infoId: response.data.infoId });
        this.props.history.push(
          `/environment/${this.state.environmentName}/info`
        );
      })
      .catch((error) => toast.error("Could not Add Info"));
  };

  async componentDidMount() {
    this.state.api.createEntity({ name: "environment" });
    this.state.api.createEntity({ name: "information" });
    await this.state.api.endpoints.environment
      .getEnvironmentByName({ name: this.state.environmentName })
      .then((response) => {
        this.setState({ environmentId: response.data.environmentId });
      })
      .catch((error) => {
        this.props.history.push("/404");
      });
  }
  //HANDLE INPUT CHANGE
  handleInputChange(event) {
    this.setState({ [event.target.name]: event.target.value });
  }

  render() {
    return (
      <div className="container">
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
            <li className="breadcrumb-item active" aria-current="page">
              Add Info
            </li>
          </ol>
        </nav>
        <div className="jumbotron">
          <h3>Add Info</h3>
          <form onSubmit={this.handleSubmit}>
            <div className="row">
              <div className="col-sm-12">
                <div className="form-row">
                  <div className="form-group col-sm-6">
                    <label htmlFor="informationName">Name Info</label>
                    <input
                      type="text"
                      id="informationName"
                      name="informationName"
                      className="form-control"
                      placeholder="Name"
                      value={this.state.informationName}
                      onChange={this.handleInputChange}
                      required
                    />
                  </div>
                </div>
              </div>
              <div className="col-sm">
                <div className="form-row">
                  <div className="form-group col-sm-6">
                    <label htmlFor="informationDescription">Description</label>
                    <textarea
                      type="text"
                      id="informationDescription"
                      name="informationDescription"
                      className="form-control"
                      rows="5"
                      placeholder="Description"
                      value={this.state.informationDescription}
                      onChange={this.handleInputChange}
                    />
                  </div>
                </div>
              </div>
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
