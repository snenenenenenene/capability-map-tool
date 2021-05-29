import React, { Component } from "react";
import { Link } from "react-router-dom";
import axios from "axios";
import toast from "react-hot-toast";

export default class AddProgram extends Component {
  constructor(props) {
    super(props);
    this.state = {
      environments: [],
      statuses: [],
      environmentName: this.props.match.params.name,
      environmentId: "",
      programName: "",
    };
    this.handleSubmit = this.handleSubmit.bind(this);
    this.handleInputChange = this.handleInputChange.bind(this);
  }

  handleSubmit = async (e) => {
    e.preventDefault();
    const formData = new FormData();
    formData.append("programName", this.state.programName);
    await axios
      .post(`${process.env.REACT_APP_API_URL}/program/`, formData)
      .then((response) => toast.success("Program Added Successfully!"))
      .catch((error) => toast.error("Could not Add Program"));
    this.props.history.push(
      `/environment/${this.state.environmentName}/program`
    );
  };

  async componentDidMount() {
    await axios
      .get(
        `${process.env.REACT_APP_API_URL}/environment/environmentname/${this.state.environmentName}`
      )
      .then((response) =>
        this.setState({ environmentId: response.data.environmentId })
      )
      .catch((error) => {
        console.log(error);
        this.props.history.push("/notfounderror");
      });
  }
  handleInputChange(event) {
    this.setState({ [event.target.name]: event.target.value });
  }

  render() {
    return (
      <div>
        <br></br>
        <nav aria-label="shadow breadcrumb">
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
              Add Program
            </li>
          </ol>
        </nav>
        <div className="jumbotron shadow">
          <h3>Add Program</h3>
          <form onSubmit={this.handleSubmit}>
            <div className="row">
              <div className="col-sm">
                <div className="form-row">
                  <div className="form-group col-md">
                    <label htmlFor="programName">Name Program</label>
                    <input
                      type="text"
                      id="programName"
                      name="programName"
                      className="form-control"
                      placeholder="Name Program"
                      value={this.state.programName}
                      onChange={this.handleInputChange}
                    />
                  </div>
                </div>
                <div className="form-row">
                  <div className="form-group col-md">
                    <label htmlFor="technology">Technology</label>
                    <input
                      type="text"
                      id="technology"
                      name="technology"
                      className="form-control"
                      placeholder="Technology"
                      value={this.state.technology}
                      onChange={this.handleInputChange}
                    />
                  </div>
                </div>
                <div className="form-row">
                  <div className="form-group col-md">
                    <label htmlFor="version">Version</label>
                    <input
                      type="text"
                      id="version"
                      name="version"
                      className="form-control"
                      placeholder="Version"
                      value={this.state.version}
                      onChange={this.handleInputChange}
                    />
                  </div>
                </div>
                <div className="form-row">
                  <div className="form-group col-md">
                    <button
                      className="btn btn-secondary btn-block"
                      style={{ marginTop: 32 }}
                      type="button"
                      onClick={this.handleSubmit}
                    >
                      Submit
                    </button>
                  </div>
                </div>
              </div>
              <div className="col-sm"></div>
              <div className="col-sm"></div>
            </div>
          </form>
        </div>
      </div>
    );
  }
}
