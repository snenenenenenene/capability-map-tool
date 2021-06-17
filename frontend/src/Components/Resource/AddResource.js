import React, { Component } from "react";
import { Link } from "react-router-dom";
import toast from "react-hot-toast";
import API from "../../Services/API";

export default class AddResource extends Component {
  constructor(props) {
    super(props);
    this.state = {
      api: new API(),
      environments: [],
      environmentName: this.props.match.params.name,
      environmentId: "",
      resourceName: "",
      resourceDescription: "",
      fullTimeEquivalentYearlyValue: "",
    };
    this.handleSubmit = this.handleSubmit.bind(this);
    this.handleInputChange = this.handleInputChange.bind(this);
  }

  handleSubmit = async (e) => {
    e.preventDefault();
    const formData = new FormData();
    formData.append(
      "fullTimeEquivalentYearlyValue",
      this.state.fullTimeEquivalentYearlyValue
    );
    formData.append("resourceName", this.state.resourceName);
    formData.append("resourceDescription", this.state.resourceDescription);
    await this.state.api.endpoints.resource
      .create(formData)
      .then((response) => {
        toast.success("Resource Added Successfully!");
        this.props.history.push(
          `/environment/${this.state.environmentName}/resource`
        );
      })
      .catch((error) => toast.error("Could not Add Resource"));
  };

  async componentDidMount() {
    this.state.api.createEntity({ name: "environment" });
    this.state.api.createEntity({ name: "resource" });

    await this.state.api.endpoints.environment
      .getEnvironmentByName({ name: this.state.environmentName })
      .then((response) =>
        this.setState({ environmentId: response.data.environmentId })
      )
      .catch((error) => {
        this.props.history.push("/404");
      });
  }

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
            <li className="breadcrumb-item">
              <Link to={`/environment/${this.state.environmentName}/resource`}>
                Resource
              </Link>
            </li>
            <li className="breadcrumb-item active" aria-current="page">
              Add Resource
            </li>
          </ol>
        </nav>
        <div className="jumbotron">
          <h3>Add Resource</h3>
          <form onSubmit={this.handleSubmit}>
            <div className="row">
              <div className="col-sm-6">
                <div className="form-row">
                  <div className="form-group col-md-6">
                    <label htmlFor="resourceName">Name Resource</label>
                    <input
                      type="text"
                      id="resourceName"
                      name="resourceName"
                      className="form-control"
                      placeholder="Name Resource"
                      value={this.state.resourceName}
                      onChange={this.handleInputChange}
                    />
                  </div>
                  <div className="form-group col-md-6">
                    <label htmlFor="fullTimeEquivalentYearlyValue">
                      Full Time Yearly Value
                    </label>
                    <input
                      type="number"
                      id="fullTimeEquivalentYearlyValue"
                      name="fullTimeEquivalentYearlyValue"
                      className="form-control"
                      placeholder="Full Time Yearly Value"
                      value={this.state.fullTimeEquivalentYearlyValue}
                      onChange={this.handleInputChange}
                    />
                  </div>
                </div>
                <div className="form-row"></div>
                <div className="form-group">
                  <label htmlFor="resourceDescription">Description</label>
                  <textarea
                    type="text"
                    id="resourceDescription"
                    name="resourceDescription"
                    className="form-control"
                    rows="5"
                    placeholder="Description"
                    value={this.state.resourceDescription}
                    onChange={this.handleInputChange}
                  />
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
