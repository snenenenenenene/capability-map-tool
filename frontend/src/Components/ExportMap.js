import React, { Component } from "react";
import { Link } from "react-router-dom";
import axios from "axios";

export default class ExportMap extends Component {
  constructor(props) {
    super(props);
    this.state = {
      environmentName: this.props.match.params.name,
      environmentId: 1,
      capabilities: 0,
      itApplications: 0,
      programs: 0,
      strategies: 0,
      strategyItems: 0,
      projects: 0,
      resources: 0,
      businessProcesses: 0,
      status: 0,
    };
  }

  async componentDidMount() {
    await axios
      .get(
        `${process.env.REACT_APP_API_URL}/environment/environmentname/${this.state.environmentName}`
      )
      .then((response) => {
        this.setState({
          environmentId: response.data.environmentId,
        });
      })
      .catch((error) => {
        console.log("environment not found");
        this.props.history.push("/notfound");
      });
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
            <li className="breadcrumb-item">Export</li>
          </ol>
        </nav>
        <div className="container jumbotron">
          <h1>Export</h1>
          <div className="card-deck"></div>
        </div>
      </div>
    );
  }
}
