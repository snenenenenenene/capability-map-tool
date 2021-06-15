import React, { Component } from "react";
import { Link } from "react-router-dom";
import toast from "react-hot-toast";
import API from "../../Services/API";

export default class EditBusinessProcess extends Component {
  constructor(props) {
    super(props);
    this.state = {
      api: new API(),
      environments: [],
      environmentName: this.props.match.params.name,
      environmentId: "",
      businessProcessId: this.props.match.params.id,
      businessProcessName: "",
      businessProcessDescription: "",
    };
    this.handleSubmit = this.handleSubmit.bind(this);
    this.handleInputChange = this.handleInputChange.bind(this);
  }

  //HANDLE SUBMIT
  handleSubmit = async (e) => {
    e.preventDefault();
    const formData = new FormData();
    formData.append("businessProcessName", this.state.businessProcessName);
    formData.append(
      "businessProcessDescription",
      this.state.businessProcessDescription
    );
    await this.state.api.endpoints.businessprocess
      .update(formData, this.state.businessProcessId)
      .then((response) => {
        toast.success("Business Process Edited Successfully!");
        this.props.history.push(
          `/environment/${this.state.environmentName}/businessprocess`
        );
      })
      .catch((error) => toast.error("Could not Edit Business Process"));
  };

  //HANDLE CAPABILITY CHANGE
  handleChange = (selectedOption) => {
    this.setState({ selectedCapabilities: selectedOption });
  };

  async componentDidMount() {
    this.state.api.createEntity({ name: "environment" });
    this.state.api.createEntity({ name: "businessprocess" });
    await this.state.api.endpoints.environment
      .getEnvironmentByName({ name: this.state.environmentName })
      .then((response) => {
        this.setState({ environmentId: response.data.environmentId });
      })
      .catch((error) => {
        this.props.history.push("/404");
      });

    await this.state.api.endpoints.businessprocess
      .getOne({ id: this.state.businessProcessId })
      .then((response) => {
        this.setState({
          businessProcessName: response.data.businessProcessName,
          businessProcessDescription: response.data.businessProcessDescription,
        });
      })
      .catch((error) => {
        toast.error("Could not load Business Process");
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
            <li className="breadcrumb-item">
              <Link
                to={`/environment/${this.state.environmentName}/businessprocess`}
              >
                Business Process
              </Link>
            </li>
            <li className="breadcrumb-item active" aria-current="page">
              {this.state.businessProcessId}
            </li>
          </ol>
        </nav>
        <div className="jumbotron">
          <h3>Edit Business Process</h3>
          <form onSubmit={this.handleSubmit}>
            <div className="row">
              <div className="col-sm-6">
                <div className="form-row">
                  <div className="form-group col-md">
                    <label htmlFor="businessProcessName">
                      Name Business Process
                    </label>
                    <input
                      type="text"
                      id="businessProcessName"
                      name="businessProcessName"
                      className="form-control"
                      placeholder="Name Business Process"
                      value={this.state.businessProcessName}
                      onChange={this.handleInputChange}
                    />
                  </div>
                </div>
                <div className="form-row"></div>
                <div className="form-group">
                  <label htmlFor="businessProcessDescription">
                    Description
                  </label>
                  <textarea
                    type="text"
                    id="businessProcessDescription"
                    name="businessProcessDescription"
                    className="form-control"
                    rows="5"
                    placeholder="Description"
                    value={this.state.businessProcessDescription}
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
