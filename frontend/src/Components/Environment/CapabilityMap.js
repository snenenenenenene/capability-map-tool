import React, { Component } from "react";
import { Link } from "react-router-dom";
import toast from "react-hot-toast";
import { Modal, OverlayTrigger, Tooltip } from "react-bootstrap";
import { PDFExport, savePDF } from "@progress/kendo-react-pdf";
import API from "../../Services/API";
import { clippingParents } from "@popperjs/core";

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
      capabilityPdfExportComponent: React.createRef(),
      capabilityMapZoom: 70,
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
          <div class='card-body zoomTarget'>
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

  zoomHandler(zoom) {
    this.setState({ capabilityMapZoom: this.state.capabilityMapZoom + zoom });
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
            <div>
              <div
                className='card strat-item'
                data-toggle='tooltip'
                data-placement='bottom'
                title={capabilityItem.strategyItem.strategyItemName}
                style={{
                  backgroundColor: itemColour,
                }}
              >
                <div className='card-body'></div>
              </div>
            </div>
            <div id='tooltip' role='tooltip'></div>
          </div>
        );
      });
    } else return;
  }
  renderCapabilityInformation(capability) {
    if (capability.capabilityInformation !== undefined) {
      return capability.capabilityInformation.map((information) => {});
    }
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
                marginBottom: 4 + "%",
                maxWidth: 20,
                minWidth: 20,
                maxHeight: 20,
                minHeight: 20,
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
  handleCapabilityExportWithComponent = (event) => {
    this.state.capabilityPdfExportComponent.current.save();
  };

  handleFileUpload = async (event) => {
    const formData = new FormData();
    formData.append("file", event.target.value);
    formData.append("environmentId", this.state.environmentId);
    await this.state.api.endpoints.environment
      .importCSV(formData)
      .then(async (response) => await this.componentDidMount())
      .catch((error) => toast.error("Could not Import Map"));
  };

  render() {
    const targetRef = React.createRef();
    return (
      <div className='capability-map zoomViewport'>
        <br></br>
        <nav aria-label='breadcrumb' className='container'>
          <ol className='breadcrumb'>
            <li className='breadcrumb-item'>
              <Link to={`/`}>Home</Link>
            </li>
            <li className='breadcrumb-item'>{this.state.environmentName}</li>
            <div className='ml-auto pdf-button'>
              {/* <span
                className='bi bi-file-earmark-spreadsheet-fill'
                aria-hidden='true'
              ></span> */}
              <label
                for='file'
                className='bi bi-file-earmark-spreadsheet-fill'
              ></label>
              <input
                name='file'
                type='file'
                id='file'
                className='inputfile'
                onChange={this.handleFileUpload}
              ></input>
            </div>
            <div
              className='pdf-button float-right'
              style={{ marginLeft: 5 }}
              onClick={this.handleExportWithComponent}
            >
              <i className='bi bi-file-earmark-pdf-fill'></i>
            </div>
          </ol>
        </nav>
        <div className='capability-map-container zoomContainer'>
          <div className='row justify-content-center' ref={targetRef}>
            <PDFExport
              ref={this.state.pdfExportComponent}
              paperSize='auto'
              margin={40}
              fileName={`${this.state.environmentName} ${new Date().getDate()}`}
              author='LEAP'
            >
              <div
                className='card-deck justify-content-center mx-auto'
                style={{ zoom: this.state.capabilityMapZoom + "%" }}
              >
                {this.capabilityMapping(this.state.capabilities)}
              </div>
            </PDFExport>
          </div>
        </div>
        <div className='zoom-dock'>
          <div>
            <button
              value='soep'
              className='zoom-button'
              onClick={() => this.zoomHandler(+10)}
            >
              <i class='bi bi-plus-lg'></i>
            </button>
          </div>
          <div>
            <button
              value='soep'
              className='zoom-button'
              onClick={() => this.zoomHandler(-10)}
            >
              <i class='bi bi-dash-lg'></i>
            </button>
          </div>
        </div>
        <div>
          <Modal
            className='capability-modal'
            show={this.state.showModal}
            onHide={() => this.handleModal()}
            centered
          >
            <Modal.Header className='capability-header' closeButton>
              <div
                className='pdf-button'
                onClick={this.handleCapabilityExportWithComponent}
              >
                <i class='bi bi-file-earmark-pdf-fill'></i>
              </div>
            </Modal.Header>
            <Modal.Body>
              <PDFExport
                ref={this.state.capabilityPdfExportComponent}
                paperSize='auto'
                margin={40}
                fileName={`${this.state.capability.capabilityName} - ${
                  this.state.environmentName
                } - ${new Date().getDate()}`}
                author='LEAP'
              >
                <div className='mx-auto justify-content-center'>
                  <h4 className='text-truncate capability-modal-title text-uppercase'>
                    {this.state.capability.capabilityName}
                  </h4>
                  <br></br>
                  <hr></hr>
                  {/* <pre>{JSON.stringify(this.state.capability, undefined, 2)}</pre> */}
                  <div className='strat-items'>
                    <th>ITEMS</th>
                    <div className='card-deck justify-content-center'>
                      {this.renderStrategyItems(this.state.capability)}
                    </div>
                  </div>
                  <br></br>
                  <table>
                    <tr>
                      <th>POC</th>
                      <td className='map-td'>
                        {this.state.capability.paceOfChange}
                      </td>
                    </tr>
                    <tr>
                      <th>TOM</th>
                      <td className='map-td'>
                        {this.state.capability.targetOperatingModel}
                      </td>
                    </tr>
                    <tr>
                      <th>AF</th>
                      <td className='map-td'>
                        {this.state.capability.applicationFit}
                      </td>
                    </tr>
                    <tr>
                      <th>RQ</th>
                      <td className='map-td'>
                        {this.state.capability.resourceQuality}
                      </td>
                    </tr>
                  </table>
                  {/* <p>{this.state.capability.projects}</p> */}
                  {/*  <p>{this.state.capability.businessprocess.length()}</p> */}
                  {this.renderCapabilityInformation(this.state.capability)}
                  {/* <p>{this.state.capability.resources.length()}</p> */}
                  {/*  <p>{this.state.capability.capabilityApplications.length()}</p> */}
                </div>
              </PDFExport>
            </Modal.Body>
          </Modal>
        </div>
      </div>
    );
  }
}
