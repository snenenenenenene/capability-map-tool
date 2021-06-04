import React, { Component } from "react";
import { Link } from "react-router-dom";
import axios from "axios";
import toast from "react-hot-toast";
import Pdf from "react-to-pdf";
import { Modal, OverlayTrigger, Tooltip } from "react-bootstrap";

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
          className="card capability-card"
          id={`capability-${capability.level}`}
        >
          <div
            className="capability-title card-header text-center text-uppercase"
            onClick={() => this.handleCapabilityClick(capability)}
          >
            {capability.capabilityName}
          </div>
          <div class="card-body">
            <div class="card-deck justify-content-center">
              {this.renderStrategyItemsinBody(capability)}
            </div>
            <div className="row">
              <div className="card-deck justify-content-center mx-auto">
                {this.capabilityMapping(capability.children)}
                <p className="card-text"></p>
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
  }

  renderStrategyItemsinBody(capability) {
    if (capability.capabilityItems !== undefined) {
      return capability.capabilityItems.map((capabilityItem) => {
        let itemColour;
        switch (capabilityItem.strategicImportance) {
          case "NONE":
            itemColour = "#fff";
            break;
          case "LOWEST":
            itemColour = "#13ff71";
            break;
          case "LOW":
            itemColour = "#b2f711";
            break;
          case "MEDIUM":
            itemColour = "#f7f711";
            break;
          case "HIGH":
            itemColour = "#f7b211";
            break;
          case "HIGHEST":
            itemColour = "#f75211";
            break;
          default:
            itemColour = "red";
        }
        return (
          <OverlayTrigger
            placement="bottom"
            overlay={
              <Tooltip id="button-tooltip-2">
                <div>{capabilityItem.strategyItem.strategyItemName}</div>
                <div>{capabilityItem.strategyItem.description}</div>
              </Tooltip>
            }
          >
            <div
              className="card"
              style={{
                margin: 3,
                maxWidth: 10,
                maxHeight: 10,
                backgroundColor: itemColour,
              }}
            >
              <div className="card-body"></div>
            </div>
          </OverlayTrigger>
        );
      });
    } else return;
  }

  renderStrategyItems(capability) {
    if (capability.capabilityItems !== undefined) {
      return capability.capabilityItems.map((capabilityItem) => {
        let itemColour;
        switch (capabilityItem.strategicImportance) {
          case "NONE":
            itemColour = "#fff";
            break;
          case "LOWEST":
            itemColour = "#13ff71";
            break;
          case "LOW":
            itemColour = "#b2f711";
            break;
          case "MEDIUM":
            itemColour = "#f7f711";
            break;
          case "HIGH":
            itemColour = "#f7b211";
            break;
          case "HIGHEST":
            itemColour = "#f75211";
            break;
          default:
            itemColour = "red";
        }
        return (
          <OverlayTrigger
            placement="bottom"
            overlay={
              <Tooltip id="button-tooltip-2">
                <div>{capabilityItem.strategyItem.strategyItemName}</div>
                <div>{capabilityItem.strategyItem.description}</div>
              </Tooltip>
            }
          >
            <div
              className="card"
              style={{
                margin: 3,
                maxWidth: 60,
                minWidth: 60,
                maxHeight: 60,
                minHeight: 60,
                backgroundColor: itemColour,
              }}
            >
              <div className="card-body"></div>
            </div>
          </OverlayTrigger>
        );
      });
    } else return;
  }

  render() {
    const targetRef = React.createRef();
    const capability = this.state.capability;
    return (
      <div>
        <br></br>
        <nav aria-label="breadcrumb">
          <ol className="breadcrumb">
            <li className="breadcrumb-item">
              <Link to={`/`}>Home</Link>
            </li>
            <li className="breadcrumb-item">{this.state.environmentName}</li>
            <Pdf
              targetRef={targetRef}
              filename="capabilitymap.pdf"
              options={{ orientation: "landscape", unit: "in" }}
            >
              {({ toPdf }) => (
                <div className="ml-auto" onClick={toPdf}>
                  <i class="bi bi-file-earmark-pdf-fill" onClick={toPdf}></i>
                </div>
              )}
            </Pdf>
          </ol>
        </nav>

        <div ref={targetRef}>
          <div className="row">
            <div className="card-deck justify-content-center mx-auto">
              {this.capabilityMapping(this.state.capabilities)}
            </div>
          </div>
        </div>
        <div>
          <Modal
            className="capability-modal"
            show={this.state.showModal}
            onHide={() => this.handleModal()}
            centered
          >
            <Modal.Header closeButton>
              <Modal.Title>{capability.capabilityName}</Modal.Title>
            </Modal.Header>
            <Modal.Body>
              <div className="card-deck  justify-content-center mx-auto">
                {this.renderStrategyItems(this.state.capability)}
              </div>
            </Modal.Body>
          </Modal>
        </div>
      </div>
    );
  }
}
