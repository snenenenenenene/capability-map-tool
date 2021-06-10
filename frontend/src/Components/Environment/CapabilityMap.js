import React, { Component } from "react";
import { Link } from "react-router-dom";
import toast from "react-hot-toast";
import { Modal } from "react-bootstrap";
import { PDFExport } from "@progress/kendo-react-pdf";
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
        <div>
          <div class="card-deck justify-content-center mx-auto">
            {this.renderStrategyItems(capability)}
          </div>
          <hr></hr>
        </div>
      );
    }
  }

  capabilityMapping(capabilities) {
    return capabilities.map((capability, i) => {
      return (
        <div
          className="card capability-card"
          id={`capability-${capability.level}`}
        >
          <div
            className="capability-title card-header text-center text-uppercase text-truncate"
            onClick={() => this.handleCapabilityClick(capability)}
          >
            {capability.capabilityName}
          </div>
          <div class="card-body zoomTarget">
            {this.strategyItemExists(capability)}
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
          <div>
            <div className="text-center" style={{ margin: 5 }}>
              {capabilityItem.strategyItem.strategyItemName}
              <div
                className="card strat-item mx-auto"
                style={{
                  backgroundColor: itemColour,
                  width: 1 + "vw",
                  height: 1 + "vw",
                }}
              ></div>
            </div>
          </div>
        );
      });
    } else return;
  }

  renderProjects(capability) {
    if (capability.projects !== undefined) {
      return capability.projects.map((project) => {
        return (
          <div className="col-sm-12">
            <div style={{ margin: 5 }}>
              <div className="card">
                <div className="card-body">{project.projectName}</div>
              </div>
            </div>
          </div>
        );
      });
    } else return;
  }
  renderInfo(capability) {
    if (capability.capabilityInformation !== undefined) {
      return capability.capabilityInformation.map((information) => {
        return (
          <div className="col-sm-12">
            <div style={{ margin: 5 }}>
              <div className="card mx-auto">
                <div
                  className="mx-auto text-uppercase"
                  style={{ marginTop: 5 }}
                >
                  {information.information.informationName}
                </div>
                <div className="card-body">
                  {information.information.informationDescription}
                </div>
                <div className="card-footer">{information.criticality}</div>
              </div>
            </div>
          </div>
        );
      });
    } else return;
  }

  renderResources(capability) {
    if (capability.resources !== undefined) {
      return capability.resources.map((resource) => {
        return (
          <div className="col-sm-12">
            <div style={{ margin: 5 }}>
              <div className="card mx-auto">
                <div className="card-body">{resource.resourceName}</div>
              </div>
            </div>
          </div>
        );
      });
    } else return;
  }

  renderBusinessProcesses(capability) {
    if (capability.businessprocess !== undefined) {
      return capability.businessprocess.map((businessProcess) => {
        return (
          <div className="col-sm-12">
            <div style={{ margin: 5 }}>
              <div className="card mx-auto">
                <div className="card-body">
                  {businessProcess.businessProcessName}
                </div>
              </div>
            </div>
          </div>
        );
      });
    } else return;
  }

  renderITApplication(capability) {
    if (capability.capabilityApplications !== undefined) {
      return capability.capabilityApplications.map((capabilityApplication) => {
        return (
          <div className="col-sm-12">
            <div style={{ margin: 5 }}>
              <div className="card mx-auto">
                <div className="card-body">
                  {capabilityApplication.application.name}
                </div>
              </div>
            </div>
          </div>
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
      .then((response) => this.componentDidMount())
      .catch((error) => toast.error("Could not Import Map"));
  };

  render() {
    const targetRef = React.createRef();
    return (
      <div className="capability-map zoomViewport">
        <br></br>
        <nav aria-label="breadcrumb" className="container">
          <ol className="breadcrumb">
            <li className="breadcrumb-item">
              <Link to={`/`}>Home</Link>
            </li>
            <li className="breadcrumb-item">{this.state.environmentName}</li>
            <div className="ml-auto pdf-button">
              {/* <span
                className='bi bi-file-earmark-spreadsheet-fill'
                aria-hidden='true'
              ></span> */}
              <label
                for="file"
                className="bi bi-file-earmark-spreadsheet-fill"
              ></label>
              <input
                name="file"
                type="file"
                id="file"
                className="inputfile"
                onChange={this.handleFileUpload}
              ></input>
            </div>
            <div
              className="pdf-button float-right"
              style={{ marginLeft: 5 }}
              onClick={this.handleExportWithComponent}
            >
              <i className="bi bi-file-earmark-pdf-fill"></i>
            </div>
          </ol>
        </nav>
        <div className="capability-map-container zoomContainer">
          <div className="row justify-content-center" ref={targetRef}>
            <PDFExport
              ref={this.state.pdfExportComponent}
              paperSize="auto"
              margin={40}
              fileName={`${this.state.environmentName} ${new Date().getDate()}`}
              author="LEAP"
            >
              <div
                className="card-deck justify-content-center mx-auto"
                style={{ zoom: this.state.capabilityMapZoom + "%" }}
              >
                {this.capabilityMapping(this.state.capabilities)}
              </div>
            </PDFExport>
          </div>
        </div>
        <div className="zoom-dock">
          <div>
            <button
              value="soep"
              className="zoom-button"
              onClick={() => this.zoomHandler(+10)}
            >
              <i class="bi bi-plus-lg"></i>
            </button>
          </div>
          <div>
            <button
              value="soep"
              className="zoom-button"
              onClick={() => this.zoomHandler(-10)}
            >
              <i class="bi bi-dash-lg"></i>
            </button>
          </div>
        </div>
        <div>
          <Modal
            size="lg"
            className="capability-modal"
            show={this.state.showModal}
            onHide={() => this.handleModal()}
            centered
          >
            <Modal.Header className="capability-header" closeButton>
              <div
                className="pdf-button"
                onClick={this.handleCapabilityExportWithComponent}
              >
                <i class="bi bi-file-earmark-pdf-fill"></i>
              </div>
            </Modal.Header>
            <Modal.Body>
              <PDFExport
                ref={this.state.capabilityPdfExportComponent}
                paperSize="auto"
                margin={40}
                fileName={`${this.state.capability.capabilityName} - ${
                  this.state.environmentName
                } - ${new Date().getDate()}`}
                author="LEAP"
              >
                <div className="mx-auto justify-content-center">
                  <h4 className="text-truncate capability-modal-title text-uppercase">
                    {this.state.capability.capabilityName}
                  </h4>
                  <br></br>
                  <hr></hr>
                  {/* <pre>{JSON.stringify(this.state.capability, undefined, 2)}</pre> */}
                  <div className="strat-items">
                    <th>ITEMS</th>
                    <div
                      className="card-deck justify-content-center"
                      style={{ marginBottom: 12 + "px" }}
                    >
                      {this.renderStrategyItems(this.state.capability)}
                    </div>
                  </div>
                  <br></br>
                  <div className="row">
                    <div className="col-sm-6">
                      <div className="form-row">
                        <table>
                          <tr>
                            <th>POC</th>
                            <td>{this.state.capability.paceOfChange}</td>
                          </tr>
                          <tr>
                            <th>TOM</th>
                            <td>
                              {this.state.capability.targetOperatingModel}
                            </td>
                          </tr>
                          <tr>
                            <th>AF</th>
                            <td>{this.state.capability.applicationFit}</td>
                          </tr>
                          <tr>
                            <th>RQ</th>
                            <td>{this.state.capability.resourceQuality}</td>
                          </tr>
                          <tr>
                            <th>DESCRIPTION</th>
                            {this.state.capability.description}
                          </tr>
                        </table>
                      </div>
                    </div>
                    <div className="col-sm-6">
                      <div className="form-row">
                        <th>PROJECTS</th>
                        {this.renderProjects(this.state.capability)}
                        <th>RESOURCES</th>
                        {this.renderResources(this.state.capability)}
                        <th>BP</th>
                        {this.renderBusinessProcesses(this.state.capability)}
                        <th>ITAPP</th>
                        {this.renderITApplication(this.state.capability)}
                        <th>INFO</th>
                        {this.renderInfo(this.state.capability)}
                      </div>
                    </div>
                  </div>
                </div>
              </PDFExport>
            </Modal.Body>
          </Modal>
        </div>
      </div>
    );
  }
}
