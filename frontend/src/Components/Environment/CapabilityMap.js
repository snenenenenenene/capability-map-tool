import React, { Component } from "react";
import { Link } from "react-router-dom";
import toast from "react-hot-toast";
import { Modal, OverlayTrigger, Tooltip } from "react-bootstrap";
import { PDFExport, savePDF } from "@progress/kendo-react-pdf";
import API from "../../Services/API";

export default class CapabilityMap extends Component {
  constructor(props) {
    super(props);
    this.state = {
      api: new API(),
      environmentName: this.props.match.params.name,
      environmentId: 1,
      capabilities: [],
      showModal: false,
      capability: {},
      pdfExportComponent: React.createRef(),
    };
  }

  handleModal() {
    this.setState({ showModal: !this.state.showModal });
  }

  handleCapabilityClick(capability) {
    this.setState({ capability: capability });
    this.handleModal();
  }

  strategyItemExists(capability) {
    if (capability.capabilityItems.length !== 0) {
      return (
        <div
          class='card-deck justify-content-center strat-items mx-auto'
          style={{ marginBottom: 10 }}
        >
          {this.renderStrategyItemsinBody(capability)}
        </div>
      );
    }
  }

  capabilityMapping(capabilities) {
    return capabilities.map((capability, i) => {
      return (
        <div
          className='card capability-card'
          id={`capability-${capability.level}`}
        >
          <div
            className='capability-title card-header text-center text-uppercase text-truncate'
            onClick={() => this.handleCapabilityClick(capability)}
          >
            {capability.capabilityName}
          </div>
          <div class='card-body'>
            {this.strategyItemExists(capability)}
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
    this.state.api.createEntity({ name: "environment" });
    await this.state.api.endpoints.environment
      .getEnvironmentByName({ name: this.state.environmentName })
      .then((response) =>
        this.setState({
          environmentId: response.data.environmentId,
        })
      )
      .catch((error) => {
        this.props.history.push("/home");
      });

    await this.state.api.endpoints.environment
      .generateCapabilityMap({ id: this.state.environmentId })
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
          <div>
            <OverlayTrigger
              placement='bottom'
              overlay={
                <Tooltip id='button-tooltip-2'>
                  <div>{capabilityItem.strategyItem.strategyItemName}</div>
                  <div>{capabilityItem.strategyItem.description}</div>
                </Tooltip>
              }
            >
              <div className=''>
                <div
                  className='card strat-item'
                  style={{
                    backgroundColor: itemColour,
                  }}
                >
                  <div className='card-body'></div>
                </div>
              </div>
            </OverlayTrigger>
          </div>
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
            placement='bottom'
            overlay={
              <Tooltip id='button-tooltip-2'>
                <div>{capabilityItem.strategyItem.strategyItemName}</div>
                <div>{capabilityItem.strategyItem.description}</div>
              </Tooltip>
            }
          >
            <div
              className='card'
              style={{
                margin: 3,
                maxWidth: 60,
                minWidth: 60,
                maxHeight: 60,
                minHeight: 60,
                backgroundColor: itemColour,
              }}
            >
              <div className='card-body'></div>
            </div>
          </OverlayTrigger>
        );
      });
    } else return;
  }

  handleExportWithComponent = (event) => {
    this.state.pdfExportComponent.current.save();
  };

  render() {
    const targetRef = React.createRef();
    const capability = this.state.capability;
    return (
      <div className='capability-map'>
        <br></br>
        <nav aria-label='breadcrumb' className='container'>
          <ol className='breadcrumb'>
            <li className='breadcrumb-item'>
              <Link to={`/`}>Home</Link>
            </li>
            <li className='breadcrumb-item'>{this.state.environmentName}</li>
            <div
              className='ml-auto pdf-button'
              onClick={this.handleExportWithComponent}
            >
              <i class='bi bi-file-earmark-pdf-fill'></i>
            </div>
          </ol>
        </nav>

        <div className='capability-map-container'>
          <div className='row justify-content-center' ref={targetRef}>
            <PDFExport
              ref={this.state.pdfExportComponent}
              paperSize='auto'
              margin={40}
              fileName={`${this.state.environmentName} ${new Date().getDate()}`}
              author='LEAP'
            >
              <div className='card-deck justify-content-center mx-auto'>
                {this.capabilityMapping(this.state.capabilities)}
              </div>
            </PDFExport>
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
              <Modal.Title className='text-truncate'>
                {capability.capabilityName}
              </Modal.Title>
            </Modal.Header>
            <Modal.Body>
              <div className='card-deck justify-content-center mx-auto'>
                {this.renderStrategyItems(this.state.capability)}
              </div>
            </Modal.Body>
          </Modal>
        </div>
      </div>
    );
  }
}
