import React, { Component } from "react";
import { Link } from "react-router-dom";
import axios from "axios";
import toast from "react-hot-toast";
import Pdf from "react-to-pdf";
import { Modal } from "react-bootstrap";

export default class CapabilityMap extends Component {
  constructor(props) {
    super(props);
    this.state = {
      environmentName: this.props.match.params.name,
      environmentId: 1,
      capabilities: [],
      showModal: false,
      capability: {},
    };
  }

  handleModal() {
    this.setState({ showModal: !this.state.showModal });
  }

  handleCapabilityClick(capability) {
    this.setState({ capability: capability });
    this.handleModal();
  }

  capabilityMapping(capabilities) {
    return capabilities.map((capability, i) => {
      return (
        <div
          className='card capability-card'
          id={`capability-${capability.level}`}
        >
          <div
            className='capability-title card-header text-center text-uppercase'
            onClick={() => this.handleCapabilityClick(capability)}
          >
            {capability.capabilityName}
          </div>
          <div class='card-body'>
            <div className='row'>
              <div className='card-deck justify-content-center mx-auto'>
                {this.capabilityMapping(capability.children)}
                <p className='card-text'></p>
              </div>
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
        if (localStorage.getItem("environment"))
          console.log(this.state.environmentName);
        localStorage.removeItem("environment");
        localStorage.setItem(
          "environment",
          JSON.stringify({
            environmentName: this.state.environmentName,
          })
        );
      })
      .catch((error) => {
        this.props.history.push("/home");
      });

    await axios
      .get(
        `${process.env.REACT_APP_API_URL}/environment/capabilitymap/${this.state.environmentId}`
      )
      .then((response) => {
        this.setState({ capabilities: response.data.capabilities });
      })
      .catch((error) => {
        toast.error("Could Not Find Capabilities");
      });
    console.log(this.state.capabilities);
  }

  render() {
    const targetRef = React.createRef();
    const capability = this.state.capability;
    return (
      <div>
        <br></br>
        <nav aria-label='breadcrumb'>
          <ol className='breadcrumb'>
            <li className='breadcrumb-item'>
              <Link to={`/`}>Home</Link>
            </li>
            <li className='breadcrumb-item'>{this.state.environmentName}</li>
            <Pdf targetRef={targetRef} filename='capabilitymap.pdf'>
              {({ toPdf }) => (
                <div className='ml-auto' onClick={toPdf}>
                  <i class='bi bi-file-earmark-pdf-fill' onClick={toPdf}></i>
                </div>
              )}
            </Pdf>
          </ol>
        </nav>

        <div ref={targetRef}>
          <div className='row'>
            <div className='card-deck justify-content-center mx-auto'>
              {this.capabilityMapping(this.state.capabilities)}
            </div>
          </div>
        </div>
        <div>
          <Modal
            className='capability-modal'
            show={this.state.showModal}
            onHide={() => this.handleModal()}
            centered
          >
            <Modal.Header closeButton>
              <Modal.Title>{capability.capabilityName}</Modal.Title>
            </Modal.Header>
            <Modal.Body>
              {/* {capability.capabilityItems.map((capabilityItem) => {
                  return (
                    <div
                      className='card'
                      style={{ margin: 10, padding: 10 }}
                    ></div>
                  );
                })} */}
            </Modal.Body>
          </Modal>
        </div>
      </div>
    );
  }
}
