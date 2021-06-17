import React, { Component } from "react";
import { Link } from "react-router-dom";
import toast from "react-hot-toast";
import { Modal } from "react-bootstrap";
import Ratings from "./Ratings";
import StatusQuickAdd from "../Status/StatusQuickAdd";
import API from "../../Services/API";

export default class AddITApplication extends Component {
  constructor(props) {
    super(props);
    this.state = {
      api: new API(),
      environments: [],
      statuses: [],
      currencies: [],
      environmentName: this.props.match.params.name,
      environmentId: "",
      statusId: "",
      strategicImportance: "",
      itApplicationName: "",
      technology: "",
      version: "",
      purchaseDate: new Date(),
      endOfLife: new Date(),
      currentScalability: 0,
      expectedScalability: 0,
      currentPerformance: 0,
      expectedPerformance: 0,
      currentSecurityLevel: 0,
      expectedSecurityLevel: 0,
      currentStability: 0,
      expectedStability: 0,
      currencyType: "EUR",
      costCurrency: "",
      currentValue: 0,
      currentYearlyCost: "",
      acceptedYearlyCost: "",
      timeValue: new Date(),
      showModal: false,
      showStatusModal: false,
    };
    this.handleSubmit = this.handleSubmit.bind(this);
    this.handleInputChange = this.handleInputChange.bind(this);
    this.updateDate = this.updateDate.bind(this);
  }
  //HANDLE SUMBIT
  handleSubmit = async (e) => {
    e.preventDefault();
    const formData = new FormData();
    formData.append("statusId", this.state.statusId);
    formData.append("name", this.state.itApplicationName);
    formData.append("version", this.state.version);
    formData.append("purchaseDate", this.state.purchaseDate);
    formData.append("endOfLife", this.state.endOfLife);
    formData.append("currentScalability", this.state.currentScalability);
    formData.append("expectedScalability", this.state.expectedScalability);
    formData.append("currentPerformance", this.state.currentPerformance);
    formData.append("expectedPerformance", this.state.expectedPerformance);
    formData.append("currentSecurityLevel", this.state.currentSecurityLevel);
    formData.append("expectedSecurityLevel", this.state.expectedSecurityLevel);
    formData.append("currentStability", this.state.currentStability);
    formData.append("expectedStability", this.state.expectedStability);
    formData.append("currentValue", this.state.currentValue);
    formData.append("currencyType", this.state.currencyType);
    formData.append(
      "costCurrency",
      parseFloat(this.state.costCurrency).toFixed(1)
    );
    formData.append(
      "currentYearlyCost",
      parseFloat(this.state.currentYearlyCost).toFixed(1)
    );
    formData.append(
      "acceptedYearlyCost",
      parseFloat(this.state.acceptedYearlyCost).toFixed(1)
    );
    formData.append("timeValue", this.state.timeValue);
    await this.state.api.endpoints.itapplication
      .create(formData)
      .then((response) => {
        toast.success("IT Application Added Successfully!");
        this.props.history.push(
          `/environment/${this.state.environmentName}/itapplication`
        );
      })
      .catch((error) => {
        toast.error("Could not Add IT Application");
      });
  };

  //FETCH STATUSES AND INSERT THEM INTO HTML SELECT
  statusListRows() {
    return this.state.statuses.map((status) => {
      return (
        <option key={status.statusId} value={status.statusId}>
          {status.validityPeriod}
        </option>
      );
    });
  }

  //PERSIST STATUSES FROM RATINGS TO ITAPPLICATION
  transferRatings(name, value) {
    this.setState({ [name]: value });
  }
  //UPDATE STATUSES WHEN ADDING A NEW DATE
  async updateDate() {
    await this.state.api.endpoints.status
      .getAll()
      .then((response) => this.setState({ statuses: response.data }))
      .catch((error) => {
        toast.error("Could not load Statuses");
      });
  }

  async componentDidMount() {
    this.state.api.createEntity({ name: "environment" });
    this.state.api.createEntity({ name: "itapplication" });
    this.state.api.createEntity({ name: "status" });
    await this.state.api.endpoints.environment
      .getEnvironmentByName({ name: this.state.environmentName })
      .then((response) =>
        this.setState({ environmentId: response.data.environmentId })
      )
      .catch((error) => {
        this.props.history.push("/404");
      });

    await this.state.api.endpoints.status
      .getAll()
      .then((response) => this.setState({ statuses: response.data }))
      .catch((error) => {
        toast.error("Could not load Statuses");
      });

    await this.state.api.endpoints.itapplication
      .getCurrencies()
      .then((response) => {
        this.setState({ currencies: response.data });
      })
      .catch((error) => {
        toast.error("No Currencies");
      });
  }

  //HANDLE INPUT CHANGE
  handleInputChange(event) {
    this.setState({ [event.target.name]: event.target.value });
  }
  //HANDLE RAITNGS MODAL
  handleModal() {
    this.setState({ showModal: !this.state.showModal });
  }

  //FETCH CURRENCIES AND INSERT THEM INTO HTML SELECT
  currencyListRow() {
    return this.state.currencies.map((currency) => {
      return (
        <option key={currency} value={currency}>
          {currency}
        </option>
      );
    });
  }
  //HANDLE STATUS MODAL
  handleStatusModal() {
    this.setState({ showStatusModal: !this.state.showStatusModal });
  }

  render() {
    return (
      <div className="container">
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
              Add IT Application
            </li>
          </ol>
        </nav>
        <div className="jumbotron shadow">
          <h3>Add IT Application</h3>
          <form onSubmit={this.handleSubmit}>
            <div className="row">
              <div className="col-sm">
                <div className="form-row">
                  <div className="form-group col-md">
                    <label htmlFor="itApplicationName">
                      Name IT-Application
                    </label>
                    <input
                      type="text"
                      id="itApplicationName"
                      name="itApplicationName"
                      className="form-control"
                      placeholder="Name IT-Application"
                      value={this.state.itApplicationName}
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
              <div className="col-sm">
                <div className="form-row">
                  <div className="form-group col-md">
                    <label htmlFor="purchaseDate">Acquisition Date</label>
                    <input
                      type="date"
                      id="purchaseDate"
                      name="purchaseDate"
                      className="form-control"
                      placeholder="Acquisition Date"
                      value={this.state.purchaseDate}
                      onChange={this.handleInputChange}
                    />
                  </div>
                </div>
                <div className="form-row">
                  <div className="form-group col-md">
                    <label htmlFor="technology">End Of Life</label>
                    <input
                      type="date"
                      id="endOfLife"
                      name="endOfLife"
                      className="form-control"
                      placeholder="End Of Life"
                      value={this.state.endOfLife}
                      onChange={this.handleInputChange}
                    />
                  </div>
                </div>
                <div className="form-row">
                  <div className="form-group col-md">
                    <label htmlFor="statusId">Validity Period</label>
                    <div className="input-group">
                      <select
                        id="statusId"
                        name="statusId"
                        className="form-control"
                        placeholder="Validity Period"
                        value={this.state.expirationDate}
                        onChange={this.handleInputChange}
                      >
                        <option
                          key="-1"
                          defaultValue="selected"
                          hidden="hidden"
                          value=""
                        >
                          Select status
                        </option>
                        {this.statusListRows()}
                      </select>
                      <button
                        style={{ marginLeft: 3 }}
                        type="button"
                        className="btn btn-sm btn-secondary"
                        onClick={() => this.handleStatusModal()}
                      >
                        Add Status
                      </button>
                    </div>
                    <Modal show={this.state.showStatusModal}>
                      <Modal.Header>Add Status</Modal.Header>
                      <Modal.Body>
                        <StatusQuickAdd
                          environmentName={this.state.environmentName}
                          updateDate={this.updateDate}
                        />
                      </Modal.Body>
                      <Modal.Footer>
                        <button
                          type="button"
                          className="btn btn-secondary"
                          onClick={() => this.handleStatusModal()}
                        >
                          Close Modal
                        </button>
                      </Modal.Footer>
                    </Modal>
                  </div>
                </div>
                <div className="form-row">
                  <div className="form-group col-md">
                    <label htmlFor="timeValue">Time Value</label>
                    <select
                      className="form-control"
                      name="timeValue"
                      placeholder="Add Time Value"
                      id="timeValue"
                      value={this.state.timeValue}
                      onChange={this.handleInputChange}
                      required
                    >
                      <option
                        key="-1"
                        defaultValue="selected"
                        hidden="hidden"
                        value=""
                      >
                        Select Time Value
                      </option>
                      <option value="TOLERATE">TOLERATE</option>
                      <option value="INVEST">INVEST</option>
                      <option value="ELIMINATE">ELIMINATE</option>
                      <option value="MIGRATE">MIGRATE</option>
                    </select>
                  </div>
                </div>
              </div>
              <div className="col-sm">
                <div className="form-row">
                  <div className="form-group col-md">
                    <label htmlFor="costCurrency">Cost Currency</label>
                    <input
                      type="number"
                      id="costCurrency"
                      name="costCurrency"
                      className="form-control"
                      placeholder="Cost Currency"
                      value={this.state.costCurrency}
                      onChange={this.handleInputChange}
                    />
                  </div>
                </div>
                <div className="form-row">
                  <div className="form-group col-md">
                    <label htmlFor="currentYearlyCost">
                      Current Total Cost Per Year
                    </label>
                    <input
                      type="number"
                      id="currentYearlyCost"
                      name="currentYearlyCost"
                      className="form-control"
                      placeholder="Current Total Cost Per Year"
                      value={this.state.currentYearlyCost}
                      onChange={this.handleInputChange}
                    />
                  </div>
                </div>
                <div className="form-row">
                  <div className="form-group col-md">
                    <label htmlFor="acceptedYearlyCost">
                      Tolerated Total Cost Per Year
                    </label>
                    <input
                      type="number"
                      id="acceptedYearlyCost"
                      name="acceptedYearlyCost"
                      className="form-control"
                      placeholder="Tolerated Total Cost Per Year"
                      value={this.state.acceptedYearlyCost}
                      onChange={this.handleInputChange}
                    />
                  </div>
                </div>
                <div className="form-row">
                  <div className="form-group col-md">
                    <label htmlFor="currencyType">Currency</label>
                    <select
                      className="form-control"
                      name="currencyType"
                      id="currencyType"
                      placeholder="Currency Type"
                      value={this.state.currencyType}
                      onChange={this.handleInputChange}
                      required
                    >
                      <option
                        key="-1"
                        defaultValue="selected"
                        hidden
                        value="EUR"
                      >
                        EUR
                      </option>
                      {this.currencyListRow()}
                    </select>
                  </div>
                </div>
                <div className="form-row">
                  <div className="form-group col-md">
                    <button
                      type="button"
                      style={{ marginTop: 32 }}
                      className="btn btn-primary btn-block"
                      onClick={() => this.handleModal()}
                    >
                      Ratings
                    </button>
                  </div>
                </div>
              </div>
            </div>
            <div>
              <Modal show={this.state.showModal}>
                <Modal.Header>Ratings</Modal.Header>
                <Modal.Body>
                  <Ratings
                    currentScalability={this.state.currentScalability}
                    expectedScalability={this.state.expectedScalability}
                    currentPerformance={this.state.currentPerformance}
                    expectedPerformance={this.state.expectedPerformance}
                    currentSecurityLevel={this.state.currentSecurityLevel}
                    expectedSecurityLevel={this.state.expectedSecurityLevel}
                    currentStability={this.state.currentStability}
                    expectedStability={this.state.expectedStability}
                    currentValue={this.state.currentValue}
                    transferRatings={this.transferRatings.bind(this)}
                  ></Ratings>
                </Modal.Body>
                <Modal.Footer>
                  <button
                    type="button"
                    className="btn btn-secondary"
                    onClick={() => this.handleModal()}
                  >
                    Close Modal
                  </button>
                </Modal.Footer>
              </Modal>
            </div>
          </form>
        </div>
      </div>
    );
  }
}
