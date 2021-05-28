import React, { Component } from "react";
import { Link } from "react-router-dom";
import axios from "axios";
import toast from "react-hot-toast";
import Pdf from "react-to-pdf";

export default class ExportMap extends Component {
  constructor(props) {
    super(props);
    this.state = {
      environmentName: this.props.match.params.name,
      environmentId: 1,
      capabilities: [],
    };
  }

  capabilityMapping() {
    if (this.state.capabilities.length > 0) {
      return this.state.capabilities.map((capability) => {
        return (
          <div
            class="card text-white bg-secondary mb-3"
            style={{ maxWidth: 10 + "rem" }}
          >
            <div class="card-header text-center text-uppercase">
              {capability.capabilityId}. {capability.capabilityName}
            </div>
            <div class="card-body">
              {() => {
                if (capability.children.length > 0) this.capabilityMapping();
              }}
              <p class="card-text"></p>
            </div>
          </div>
        );
      });
    } else {
      return <h2>No Capabilities</h2>;
    }
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

    await axios
      .get(
        `${process.env.REACT_APP_API_URL}/capability/all-capabilities-by-environmentid/${this.state.environmentId}`
      )
      .then((response) => {
        this.setState({ capabilities: response.data });
      })
      .catch((error) => {
        console.log(error);
        toast.error("Could Not Find Capabilities");
      });
  }

  render() {
    const targetRef = React.createRef();
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
          <h1 className="display-4" style={{ display: "inline-block" }}>
            Export
          </h1>
          <Pdf targetRef={targetRef} filename="capabilitymap.pdf">
            {({ toPdf }) => (
              <button className="float-right btn btn-danger" onClick={toPdf}>
                Generate Pdf
              </button>
            )}
          </Pdf>

          <div className="card-deck justify-content-center" ref={targetRef}>
            {this.capabilityMapping()}
          </div>
        </div>
      </div>
    );
  }
}
