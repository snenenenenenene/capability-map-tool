import React, { Component } from "react";
import { Link } from "react-router-dom";
import ReactStars from "react-stars";
import axios from "axios";
import { Modal } from "react-bootstrap";
import StatusQuickAdd from "../Status/StatusQuickAdd";
import toast from "react-hot-toast";
import Select from "react-select";

export default class AddCapability extends Component {
  constructor(props) {
    super(props);
    this.state = {
      statuses: [],
      environments: [],
      capabilities: [],
      strategyItems: [],
      selectedItems: [],

      environmentName: this.props.match.params.name,
      environmentId: "",
      capabilityName: "",
      parentCapability: 0,
      description: "",
      paceOfChange: "",
      targetOperatingModel: "",
      informationQuality: "",
      applicationFit: "",
      resourcesQuality: "",
      statusId: "",
      showModal: false,
      immediate: true,
      setFocusOnError: true,
      clearInputOnReset: false,
      showItemModal: false,
    };
    this.handleSubmit = this.handleSubmit.bind(this);
    this.handleInputChange = this.handleInputChange.bind(this);
    this.updateDate = this.updateDate.bind(this);
  }

  handleSubmit = async (e) => {
    e.preventDefault();
    const formData = new FormData();
    formData.append("environmentName", this.state.environmentName);
    formData.append("environmentId", this.state.environmentId);
    formData.append("capabilityName", this.state.capabilityName);
    formData.append("parentCapabilityId", this.state.parentCapability);
    formData.append("paceOfChange", this.state.paceOfChange);
    formData.append("targetOperatingModel", this.state.targetOperatingModel);
    formData.append("informationQuality", this.state.informationQuality);
    formData.append("applicationFit", this.state.applicationFit);
    formData.append("resourceQuality", this.state.resourcesQuality);
    formData.append("statusId", this.state.statusId);
    formData.append("level", "ONE");
    await axios
      .post(`${process.env.REACT_APP_API_URL}/capability/`, formData)
      .then((response) => {
        toast.success("Capability Added Successfully!");
        this.setState({ capabilityId: response.data.capabilityId });
      })
      .catch((error) => toast.error("Could not Add Capability"));
    if (this.state.selectedItems.length !== 0) {
      let promises = [];

      this.state.selectedItems.forEach((item) => {
        const formData = new FormData();
        formData.append("itemId", item.itemId);
        formData.append("capabilityId", this.state.capabilityId);
        formData.append("strategicImportance", this.state.strategicImportance);
        promises.push(
          axios.post(
            `${process.env.REACT_APP_API_URL}/capabilityitem/`,
            formData
          )
        );

        Promise.all(promises)
          .then()
          .catch((error) => toast.error("Failed to Connect to Capability"));
      });
    }
    this.props.history.push(
      `/environment/${this.state.environmentName}/strategyitem`
    );
    this.props.history.push(
      `/environment/${this.state.environmentName}/capability`
    );
  };

  async componentDidMount() {
    await axios
      .get(
        `${process.env.REACT_APP_API_URL}/environment/environmentname/${this.state.environmentName}`
      )
      .then((response) => {
        this.setState({ environmentId: response.data.environmentId });
      })
      .catch((error) => {
        console.log(error);
        this.props.history.push("/notfounderror");
      });

    await axios
      .get(`${process.env.REACT_APP_API_URL}/status/`)
      .then((response) => this.setState({ statuses: response.data }))
      .catch((error) => {
        toast.error("Could not load Statuses");
      });

    await axios
      .get(
        `${process.env.REACT_APP_API_URL}/capability/all-capabilities-by-environmentid/${this.state.environmentId}`
      )
      .then((response) => this.setState({ capabilities: response.data }))
      .catch((error) => {
        toast.error("Could not load Capabilities");
      });

    await axios
      .get(`${process.env.REACT_APP_API_URL}/strategyitem/`)
      .then((response) => {
        response.data.forEach((item) => {
          item.label = item.strategyItemName;
          item.value = item.itemId;
        });
        this.setState({ strategyItems: response.data });
      })
      .catch((error) => {
        toast.error("Could not load Strategy Items");
      });
  }

  handleItemModal() {
    this.setState({ showItemModal: !this.state.showItemModal });
  }

  handleInputChange(event) {
    this.setState({ [event.target.name]: event.target.value });
  }

  statusListRows() {
    return this.state.statuses.map((status) => {
      return (
        <option key={status.statusId} value={status.statusId}>
          {status.validityPeriod}
        </option>
      );
    });
  }

  capabilityListRows() {
    return this.state.capabilities.map((capability) => {
      return (
        <option key={capability.capabilityId} value={capability.capabilityId}>
          {capability.capabilityName}
        </option>
      );
    });
  }

  ratingChanged = (newRating) => {
    console.log(newRating);
    this.setState({ resourcesQuality: newRating });
  };

  handleChange = (selectedOption) => {
    this.setState({ selectedItems: selectedOption });
  };

  async updateDate() {
    await axios
      .get(`${process.env.REACT_APP_API_URL}/status/`)
      .then((response) => this.setState({ statuses: response.data }))
      .catch((error) => {
        console.log(error);
        toast.error("Could not Update Statuses");
      });
  }

  handleModal() {
    this.setState({ showModal: !this.state.showModal });
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
            <li className="breadcrumb-item active" aria-current="page">
              Add Capability
            </li>
          </ol>
        </nav>
        <div className="jumbotron">
          <h3>Add Capability</h3>
          <form onSubmit={this.handleSubmit}>
            <div className="row">
              <div className="col-sm-6">
                <div className="form-row">
                  <div className="form-group col-md-6">
                    <label htmlFor="nameCapability">Name Capability</label>
                    <input
                      type="text"
                      id="capabilityName"
                      name="capabilityName"
                      className="form-control"
                      placeholder="Name Capability"
                      value={this.state.capabilityName}
                      onChange={this.handleInputChange}
                      required
                    />
                  </div>
                </div>
                <div className="form-row">
                  <div className="form-group col-md-6">
                    <label htmlFor="paceOfChange">Parent Capability</label>
                    <select
                      className="form-control"
                      name="parentCapability"
                      id="parentCapability"
                      placeholder="Add Parent Capability"
                      value={this.state.parentCapabilityId}
                      onChange={this.handleInputChange}
                      required
                    >
                      <option key="-1" defaultValue="selected" value={0}>
                        None
                      </option>
                      {this.capabilityListRows()}
                    </select>
                  </div>
                  <div className="form-group col-md-6">
                    <label htmlFor="level">Capability Level</label>
                    <select
                      className="form-control"
                      name="level"
                      id="level"
                      placeholder="Add Level"
                      value={this.state.parentCapabilityId}
                      onChange={this.handleInputChange}
                      required
                    >
                      <option defaultValue="selected" value="ONE">
                        {this.state.parentCapabilityId}
                      </option>
                    </select>
                  </div>
                </div>
                <div className="form-group">
                  <label htmlFor="description">Description</label>
                  <textarea
                    type="text"
                    id="description"
                    name="description"
                    className="form-control"
                    rows="5"
                    placeholder="Description"
                    value={this.state.description}
                    onChange={this.handleInputChange}
                  />
                </div>
              </div>
              <div className="col-sm-6">
                <div className="form-row">
                  <div className="form-group col-md-6">
                    <label htmlFor="paceOfChange">Pace of Change</label>
                    <select
                      className="form-control"
                      name="paceOfChange"
                      placeholder="Add Pace of Change"
                      id="paceOfChange"
                      value={this.state.paceOfChange}
                      onChange={this.handleInputChange}
                      required
                    >
                      <option
                        key="-1"
                        defaultValue="selected"
                        hidden="hidden"
                        value=""
                      >
                        Select Pace of Change
                      </option>
                      <option value="true">True</option>
                      <option value="false">False</option>
                    </select>
                  </div>
                  <div className="form-group col-md-6">
                    <label htmlFor="informationQuality">
                      Information Quality
                    </label>
                    <select
                      className="form-control"
                      name="informationQuality"
                      placeholder="Add Information Quality"
                      id="informationQuality"
                      value={this.state.informationQuality}
                      onChange={this.handleInputChange}
                      required
                    >
                      <option
                        key="-1"
                        defaultValue="selected"
                        hidden="hidden"
                        value=""
                      >
                        Select Information Quality
                      </option>
                      <option>1</option>
                      <option>2</option>
                      <option>3</option>
                      <option>4</option>
                      <option>5</option>
                    </select>
                  </div>
                </div>
                <div className="form-row">
                  <div className="form-group col-md-6">
                    <label htmlFor="targetOperatingModel">TOM</label>
                    <select
                      className="form-control"
                      name="targetOperatingModel"
                      placeholder="Add targetOperatingModel"
                      id="targetOperatingModel"
                      value={this.state.targetOperatingModel}
                      onChange={this.handleInputChange}
                      required
                    >
                      <option
                        key="-1"
                        defaultValue="selected"
                        hidden="hidden"
                        value=""
                      >
                        Select TOM
                      </option>
                      <option value="Coordination">Coordination</option>
                      <option value="Coordination">Diversification</option>
                      <option value="Coordination">Replication</option>
                      <option value="Coordination">Unification</option>
                    </select>
                  </div>
                  <div className="form-group col-md-6">
                    <label htmlFor="applicationFit">Application Fit</label>
                    <select
                      className="form-control"
                      name="applicationFit"
                      placeholder="Add Application Fit"
                      id="applicationFit"
                      value={this.state.applicationFit}
                      onChange={this.handleInputChange}
                      required
                    >
                      <option
                        key="-1"
                        defaultValue="selected"
                        hidden="hidden"
                        value=""
                      >
                        Select Application Fit
                      </option>
                      <option>1</option>
                      <option>2</option>
                      <option>3</option>
                      <option>4</option>
                      <option>5</option>
                    </select>
                  </div>
                </div>
                <div className="form-row">
                  <div className="form-group col-md">
                    <label htmlFor="resourcesQuality">Resources Quality</label>
                    <ReactStars
                      count={5}
                      onChange={this.ratingChanged}
                      size={24}
                      half={false}
                      color2={"#ffd700"}
                      value={this.state.resourcesQuality}
                      required
                    />
                  </div>
                  <div className="form-group col-md">
                    <label htmlFor="showItem">Strategy Item (Optional)</label>
                    <button
                      className="btn btn-secondary btn-block"
                      type="button"
                      onClick={() => this.handleItemModal()}
                    >
                      Add Strategy Item
                    </button>
                    <Modal show={this.state.showItemModal}>
                      <Modal.Header>Add Items</Modal.Header>
                      <Modal.Body>
                        <label htmlFor="itemId">Strategy Items</label>
                        <Select
                          options={this.state.strategyItems}
                          isMulti
                          closeMenuOnSelect={false}
                          onChange={this.handleChange}
                          placeholder="Optional"
                        />
                        <label htmlFor="strategicImportance">Importance</label>
                        <select
                          className="form-control"
                          name="strategicImportance"
                          id="strategicImportance"
                          placeholder="Add Importance"
                          value={this.state.strategicImportance}
                          onChange={this.handleInputChange}
                        >
                          <option defaultValue="selected" hidden value={null}>
                            Optional
                          </option>
                          <option value="NONE">None</option>
                          <option value="LOWEST">Lowest</option>
                          <option value="MEDIUM">Medium</option>
                          <option value="HIGH">High</option>
                          <option value="HIGHEST">Highest</option>
                        </select>
                      </Modal.Body>
                      <Modal.Footer>
                        <button
                          type="button"
                          className="btn btn-secondary"
                          onClick={() => this.handleItemModal()}
                        >
                          Close Modal
                        </button>
                      </Modal.Footer>
                    </Modal>
                  </div>
                </div>
                <div className="form-row">
                  <div className="form-group col-md-12">
                    <label htmlFor="statusId">Validity Period</label>

                    <div className="input-group">
                      <select
                        id="statusId"
                        name="statusId"
                        className="form-control"
                        placeholder="Validity Period"
                        value={this.state.expirationDate}
                        required
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
                        className="btn btn-secondary"
                        onClick={() => this.handleModal()}
                      >
                        Add Status
                      </button>
                      <Modal show={this.state.showModal}>
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
                            onClick={() => this.handleModal()}
                          >
                            Close Modal
                          </button>
                        </Modal.Footer>
                      </Modal>
                    </div>
                  </div>
                </div>
              </div>
            </div>
            <button
              className="btn btn-primary"
              type="button"
              onClick={this.handleSubmit}
            >
              Submit
            </button>
          </form>
        </div>
      </div>
    );
  }
}
