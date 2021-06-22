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
      capabilityMapLayerLevel: 3,
    };
  }

  //TOGGLE MODAL
  handleModal() {
    this.setState({ showModal: !this.state.showModal });
  }

  //TOGGLE MODAL WHEN CLICKING CAPABILITY
  handleCapabilityClick(capability) {
    this.setState({ capability: capability });
    this.handleModal();
  }

  //CHECK IF STRATEGY ITEMS EXIST
  strategyItemExists(capability) {
    if (capability.capabilityItems.length !== 0) {
      return (
        <div>
          <div class="card-deck justify-content-center mx-auto">
            {this.renderStrategyItems(capability)}
          </div>
        </div>
      );
    }
  }
  //CHECK IF PROJECTS EXIST
  projectExists = (capability) => {
    if (
      typeof capability.projects !== "undefined" &&
      capability.projects.length > 0
    ) {
      return (
        <div className="col-md-12">
          <th>PROJECTS</th>
          <div class="card-deck">{this.renderProjects(capability)}</div>
        </div>
      );
    }
  };
  //CHECK IF RESOURCES EXIST
  resourceExists = (capability) => {
    if (
      typeof capability.resources !== "undefined" &&
      capability.resources.length > 0
    ) {
      return (
        <div className="col-md-12">
          <th>RESOURCES</th>
          <div class="card-deck">{this.renderResources(capability)}</div>
        </div>
      );
    }
  };

  //CHECK IF ITAPPLICATIONS EXIST
  itApplicationExists = (capability) => {
    if (
      typeof capability.capabilityApplications !== "undefined" &&
      capability.capabilityApplications.length > 0
    ) {
      return (
        <div className="col-md-12">
          <th>ITAPP</th>
          <div class="card-deck">{this.renderITApplication(capability)}</div>
        </div>
      );
    }
  };

  //CHECK IF BUSINESSPROCESSES EXIST
  businessProcessExists = (capability) => {
    if (
      typeof capability.businessprocess !== "undefined" &&
      capability.businessprocess.length > 0
    ) {
      return (
        <div className="col-md-12">
          <th>BP</th>
          <div class="card-deck">
            {this.renderBusinessProcesses(capability)}
          </div>
        </div>
      );
    }
  };

  //CHECK IF INFORMATIONS EXIST
  informationExists = (capability) => {
    if (
      typeof capability.capabilityInformation !== "undefined" &&
      capability.capabilityInformation.length > 0
    ) {
      return (
        <div className="col-md-12">
          <th>INFO</th>
          <div class="card-deck">{this.renderInfo(capability)}</div>
        </div>
      );
    }
  };

  //MAP CAPABILITIES TO THEIR HTML COUNTERPARTS
  capabilityMapping(capabilities) {
    let today = new Date().toISOString().slice(0, 10);
    return capabilities.map((capability, i) => {
      let itemColour;
      if (capability.status.validityPeriod < today) {
        itemColour = "#fff4ed80";
      } else if (capability.status.validityPeriod === today) {
        itemColour = "#fff6d680";
      } else if (capability.status.validityPeriod > today) {
        itemColour = "#fff";
      }
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
          {this.strategyItemExists(capability)}
          <div className="card-body" style={{ backgroundColor: itemColour }}>
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
    this.state.api.createEntity({ name: "csv" });
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
      .generateCapabilityMap({
        id: this.state.environmentId,
        level: this.state.capabilityMapLayerLevel,
      })
      .then((response) => {
        this.setState({ capabilities: response.data.capabilities });
      })
      .catch((error) => {
        toast.error("Could Not Find Capabilities");
      });
  }

  //HANDLE ZOOM WHEN CLICKING THE MINIMIZE OR MAXIMIZE BUTTONS
  zoomHandler(zoom) {
    this.setState({ capabilityMapZoom: this.state.capabilityMapZoom + zoom });
  }

  capabilityMapLayerLevelHandler(level) {
    if (this.state.capabilityMapLayerLevel < 1) {
      this.setState({ capabilityMapLayerLevel: 1 });
    } else if (this.state.capabilityMapLayerLevel > 3) {
      this.setState({ capabilityMapLayerLevel: 3 });
    } else {
      console.log(this.state.capabilityMapLayerLevel + level);
      this.setState({
        capabilityMapLayerLevel: this.state.capabilityMapLayerLevel + level,
      });
      this.componentDidMount();
    }
  }

  //RENDER STRATEGY ITEMS AND ADD COLOURS BASED ON THEIR STRATEGIC IMPORTANCE
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
                className="card strat-item mx-auto text-truncate"
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

  //RENDER PROJECTS AS CARDS
  renderProjects(capability) {
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
  }

  //RENDER INFO AS CARDS
  renderInfo(capability) {
    return capability.capabilityInformation.map((information) => {
      return (
        <div className="col-sm-12">
          <div style={{ margin: 5 }}>
            <div className="card mx-auto">
              <div className="mx-auto text-uppercase" style={{ marginTop: 5 }}>
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
  }

  //RENDER RESOURCES AS CARDS
  renderResources(capability) {
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
  }

  //RENDER BUSINESSPROCESSES AS CARDS
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

  //RENDER IT APPLICATIONS AS CARDS
  renderITApplication(capability) {
    if (capability.capabilityApplications !== undefined) {
      return capability.capabilityApplications.map((capabilityApplication) => {
        return (
          <div className="col-sm-12">
            <div style={{ margin: 5 }}>
              <div className="card mx-auto">
                <div className="card-body">
                  {capabilityApplication.application.name}
                  <table>
                    <tr>
                      <th>Availability</th>
                      <td>{capabilityApplication.availability}</td>
                    </tr>
                    <tr>
                      <th>Efficiency Support</th>
                      <td>{capabilityApplication.efficiencySupport}</td>
                    </tr>
                    <tr>
                      <th>Functional Coverage</th>
                      <td>{capabilityApplication.functionalCoverage}</td>
                    </tr>
                    <tr>
                      <th>Correctness Business Fit</th>
                      <td>{capabilityApplication.correctnessBusinessFit}</td>
                    </tr>
                    <tr>
                      <th>Future Potential</th>
                      {capabilityApplication.futurePotential}
                    </tr>
                    <tr>
                      <th>Completeness</th>
                      {capabilityApplication.completeness}
                    </tr>
                    <tr>
                      <th>Correctness Information Fit</th>
                      {capabilityApplication.correctnessInformationFit}
                    </tr>
                  </table>
                </div>
              </div>
            </div>
          </div>
        );
      });
    } else return;
  }

  //EXPORT ENTIRE MAP
  handleExportWithComponent = (event) => {
    this.state.pdfExportComponent.current.save();
  };

  //EXPORT CAPABILITY
  handleCapabilityExportWithComponent = (event) => {
    this.state.capabilityPdfExportComponent.current.save();
  };

  //HANDLE UPLOADING CSV
  handleFileUpload = async (event) => {
    const formData = new FormData();
    formData.append("file", event.target.files[0]);
    formData.append("environmentId", this.state.environmentId);
    await this.state.api.endpoints.csv
      .importCSV(formData)
      .then((response) => {
        toast.success("CSV Imported");
        this.componentDidMount();
      })
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
              value="levelup"
              className="zoom-button"
              onClick={() => this.capabilityMapLayerLevelHandler(+1)}
            >
              <i class="bi bi-chevron-up"></i>
            </button>
          </div>
          <div>
            <button
              value="leveldown"
              className="zoom-button"
              onClick={() => this.capabilityMapLayerLevelHandler(-1)}
            >
              <i class="bi bi-chevron-down"></i>
            </button>
          </div>
          <div>
            <button
              value="zoomout"
              className="zoom-button"
              onClick={() => this.zoomHandler(+10)}
            >
              <i class="bi bi-plus-lg"></i>
            </button>
          </div>
          <div>
            <button
              value="zoomout"
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
                fileName={`${this.state.capability.capabilityName} - ${this.state.environmentName}`}
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
                        {this.projectExists(this.state.capability)}
                        {this.resourceExists(this.state.capability)}
                        {this.itApplicationExists(this.state.capability)}
                        {this.businessProcessExists(this.state.capability)}
                        {this.informationExists(this.state.capability)}
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
