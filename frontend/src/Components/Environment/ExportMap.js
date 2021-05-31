import React, { Component } from "react";
import { Link } from "react-router-dom";
import axios from "axios";
import toast from "react-hot-toast";
import Pdf from "react-to-pdf";
import { Modal } from "react-bootstrap";

export default class ExportMap extends Component {
  constructor(props) {
    super(props);
    this.state = {
      environmentName: this.props.match.params.name,
      environmentId: 1,
      capabilities: [],
      showModal: false,
    };
  }

  handleModal() {
    this.setState({ showModal: !this.state.showModal });
  }

  generateModal() {
    if (this.state.showModal === true) {
      return (
        <div style={{ backgroundColor: "red" }}>
          <h1>hi</h1>
        </div>
        // <Modal show={true}>
        //   <Modal.Header>{capabilityId}</Modal.Header>
        //   <Modal.Body>
        //     <p>{capabilityId}</p>
        //   </Modal.Body>
        //   <Modal.Footer>
        //     <button type="button" className="btn btn-secondary">
        //       Close Modal
        //     </button>
        //   </Modal.Footer>
        // </Modal>
      );
    }
    return;
  }

  capabilityMapping(capabilities) {
    return capabilities.map((capability, i) => {
      return (
        <div
          className="card capability-card"
          id={`capability-${capability.level}`}
        >
          <div
            className="card-header text-center text-uppercase"
            onClick={() => this.handleModal()}
          >
            {i} {capability.capabilityName}
          </div>
          <div class="card-body">
            {}
            <div className="card-deck justify-content-center">
              {this.capabilityMapping(capability.children)}
              <p className="card-text"></p>
            </div>
          </div>
        </div>
      );
    });
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
        `${process.env.REACT_APP_API_URL}/environment/capabilitymap/${this.state.environmentId}`
      )
      .then((response) => {
        this.setState({ capabilities: response.data.capabilities });
      })
      .catch((error) => {
        console.log(error);
        toast.error("Could Not Find Capabilities");
      });
    console.log(this.state.capabilities);
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

          <div ref={targetRef} className="card-deck justify-content-center">
            {this.capabilityMapping(this.state.capabilities)}
          </div>
        </div>
      </div>
    );
  }
}
