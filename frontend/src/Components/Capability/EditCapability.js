import React, { Component } from "react";
import { Link } from "react-router-dom";
import axios from "axios";
import { Modal } from "react-bootstrap";
import StatusQuickAdd from "../Status/StatusQuickAdd";
import toast from "react-hot-toast";
import Select from "react-select";
import ReactStars from "react-stars";
export default class EditCapability extends Component {
  constructor(props) {
    super(props);
    this.state = {
      statuses: [],
      environments: [],
      capabilities: [],
      strategyItems: [],
      selectedItems: [],

      environmentName: this.props.match.params.name,
      capabilityId: this.props.match.params.id,
      environmentId: "",
      capabilityName: "",
      parentCapabilityId: 0,
      description: "",
      paceOfChange: "",
      targetOperatingModel: "",
      informationQuality: "",
      applicationFit: "",
      resourceQuality: "",
      statusId: "",
      showModal: false,
      showItemModal: false,
    };
    this.handleSubmit = this.handleSubmit.bind(this);
    this.handleInputChange = this.handleInputChange.bind(this);
    this.updateDate = this.updateDate.bind(this);
  }
  handleItemModal() {
    this.setState({ showItemModal: !this.state.showItemModal });
  }
  handleModal() {
    this.setState({ showModal: !this.state.showModal });
  }
  async updateDate() {
    await axios
      .get(`${process.env.REACT_APP_API_URL}/status/`)
      .then((response) => this.setState({ statuses: response.data }))
      .catch((error) => {
        toast.error("Could not Update Statuses");
      });
  }

  ratingChanged = (newRating) => {
    this.setState({ resourceQuality: newRating });
  };
  handleSubmit = async (e) => {
    e.preventDefault();
    const formData = new FormData();
    formData.append("environmentName", this.state.environmentName);
    formData.append("environmentId", this.state.environmentId);
    formData.append("capabilityName", this.state.capabilityName);
    formData.append("parentCapabilityId", this.state.parentCapabilityId);
    formData.append("paceOfChange", this.state.paceOfChange);
    formData.append("targetOperatingModel", this.state.targetOperatingModel);
    formData.append("informationQuality", this.state.informationQuality);
    formData.append("applicationFit", this.state.applicationFit);
    formData.append("resourceQuality", this.state.resourceQuality);
    formData.append("statusId", this.state.statusId);
    formData.append("level", this.state.capabilityLevel);
    await axios
      .put(
        `${process.env.REACT_APP_API_URL}/capability/${this.state.capabilityId}`,
        formData
      )
      .then((response) => {
        toast.success("Capability Successfully Updated!");
        this.props.history.push(
          `/environment/${this.state.environmentName}/capability`
        );
      })
      .catch((error) => {
        toast.error("Could not Update Capability");
      });
  };

  async componentDidMount() {
    await axios
      .get(
        `${process.env.REACT_APP_API_URL}/environment/environmentname/${this.state.environmentName}`
      )
      .then((response) =>
        this.setState({ environmentId: response.data.environmentId })
      )
      .catch((error) => {
        this.props.history.push("/404");
      });

    await axios
      .get(`${process.env.REACT_APP_API_URL}/status/`)
      .then((response) => {
        response.data.forEach((status) => {
          status.label = status.validityPeriod;
          status.value = status.statusId;
        });
        this.setState({ statuses: response.data });
      })
      .catch((error) => {
        toast.error("Could not Load Statuses");
      });

    await axios
      .get(`${process.env.REACT_APP_API_URL}/capability/`)
      .then((response) => {
        response.data.forEach((cap) => {
          cap.label = `${cap.capabilityName} - lvl: ${cap.level}`;
          cap.value = cap.capabilityId;
        });
        this.setState({ capabilities: response.data });
      })
      .catch((error) => {
        toast.error("Could not Load Capabilities");
      });

    await axios
      .get(
        `${process.env.REACT_APP_API_URL}/capability/${this.state.capabilityId}`
      )
      .then((response) => {
        this.setState({
          environmentId: response.data.environment.environmentId,
          oldCapabilityName: response.data.capabilityName,
          capabilityName: response.data.capabilityName,
          parentCapabilityId: response.data.parentCapabilityId,
          paceOfChange: response.data.paceOfChange,
          targetOperatingModel: response.data.targetOperatingModel,
          informationQuality: response.data.informationQuality,
          applicationFit: response.data.applicationFit,
          resourceQuality: response.data.resourceQuality,
          statusId: response.data.status.statusId,
          capabilityLevel: response.data.level,
          validityPeriod: response.data.status.validityPeriod,
        });
      })
      .catch((error) => {
        toast.error("Could not Find Capability");
      });
  }

  handleInputChange(event) {
    this.setState({ [event.target.name]: event.target.value });
  }

  render() {
    return (
      <div className='container'>
        <br></br>
        <nav aria-label='breadcrumb'>
          <ol className='breadcrumb'>
            <li className='breadcrumb-item'>
              <Link to={`/`}>Home</Link>
            </li>
            <li className='breadcrumb-item'>
              <Link to={`/environment/${this.state.environmentName}`}>
                {this.state.environmentName}
              </Link>
            </li>
            <li className='breadcrumb-item'>
              <Link
                to={`/environment/${this.state.environmentName}/capability`}
              >
                Capability
              </Link>
            </li>
            <li className='breadcrumb-item'>{this.state.capabilityId}</li>
            <li className='breadcrumb-item active' aria-current='page'>
              Edit Capability
            </li>
          </ol>
        </nav>
        <div className='jumbotron'>
          <h3>Edit Capability</h3>
          <form onSubmit={this.handleSubmit}>
            <div className='row'>
              <div className='col-sm-6'>
                <div className='form-row'>
                  <div className='form-group col-md-6'>
                    <label htmlFor='nameCapability'>Name Capability</label>
                    <input
                      type='text'
                      id='capabilityName'
                      name='capabilityName'
                      className='form-control'
                      placeholder='Name Capability'
                      value={this.state.capabilityName}
                      onChange={this.handleInputChange}
                      required
                    />
                  </div>
                </div>
                <div className='form-row'>
                  <div className='form-group col-md'>
                    <label htmlFor='paceOfChange'>Parent Capability</label>
                    <Select
                      options={this.state.capabilities}
                      value={this.state.capabilities.filter(
                        (capability) =>
                          capability.capabilityId ===
                          this.state.parentCapabilityId
                      )}
                      name='parentCapability'
                      id='parentCapability'
                      placeholder='Add Parent Capability'
                      noOptionsMessage={() => "No Level 1 Capabilities"}
                      onChange={(cap) => {
                        if (cap) {
                          this.setState({
                            parentCapabilityId: cap.capabilityId,
                          });
                        } else {
                          this.setState({ parentCapabilityId: 0 });
                        }
                      }}
                      isClearable={true}
                      required
                    ></Select>
                  </div>
                </div>
                <div className='form-group'>
                  <label htmlFor='description'>Description</label>
                  <textarea
                    type='text'
                    id='description'
                    name='description'
                    className='form-control'
                    rows='5'
                    placeholder='Description'
                    value={this.state.description}
                    onChange={this.handleInputChange}
                  />
                </div>
              </div>
              <div className='col-sm-6'>
                <div className='form-row'>
                  <div className='form-group col-md-6'>
                    <label htmlFor='paceOfChange'>Pace of Change</label>
                    <select
                      className='form-control'
                      name='paceOfChange'
                      placeholder='Add Pace of Change'
                      id='paceOfChange'
                      value={this.state.paceOfChange}
                      onChange={this.handleInputChange}
                      required
                    >
                      <option
                        key='-1'
                        defaultValue='selected'
                        hidden='hidden'
                        value=''
                      >
                        Select Pace of Change
                      </option>
                      <option value='true'>True</option>
                      <option value='false'>False</option>
                    </select>
                  </div>
                  <div className='form-group col-md-6'>
                    <label htmlFor='informationQuality'>
                      Information Quality
                    </label>
                    <select
                      className='form-control'
                      name='informationQuality'
                      placeholder='Add Information Quality'
                      id='informationQuality'
                      value={this.state.informationQuality}
                      onChange={this.handleInputChange}
                      required
                    >
                      <option
                        key='-1'
                        defaultValue='selected'
                        hidden='hidden'
                        value=''
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
                <div className='form-row'>
                  <div className='form-group col-md-6'>
                    <label htmlFor='targetOperatingModel'>TOM</label>
                    <select
                      className='form-control'
                      name='targetOperatingModel'
                      placeholder='Add targetOperatingModel'
                      id='targetOperatingModel'
                      value={this.state.targetOperatingModel}
                      onChange={this.handleInputChange}
                      required
                    >
                      <option
                        key='-1'
                        defaultValue='selected'
                        hidden='hidden'
                        value=''
                      >
                        Select TOM
                      </option>
                      <option value='Coordination'>Coordination</option>
                      <option value='Diversification'>Diversification</option>
                      <option value='Replication'>Replication</option>
                      <option value='Unification'>Unification</option>
                    </select>
                  </div>
                  <div className='form-group col-md-6'>
                    <label htmlFor='applicationFit'>Application Fit</label>
                    <select
                      className='form-control'
                      name='applicationFit'
                      placeholder='Add Application Fit'
                      id='applicationFit'
                      value={this.state.applicationFit}
                      onChange={this.handleInputChange}
                      required
                    >
                      <option
                        key='-1'
                        defaultValue='selected'
                        hidden='hidden'
                        value=''
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
                <div className='form-row'>
                  <div className='form-group col-md-9'>
                    <label htmlFor='statusId'>Status</label>
                    <Select
                      value={this.state.statuses.filter(
                        (status) => status.statusId === this.state.statusId
                      )}
                      id='statusId'
                      name='statusId'
                      placeholder='Validity Period'
                      options={this.state.statuses}
                      required
                      onChange={(statusId) => {
                        this.setState({ statusId: statusId });
                      }}
                    ></Select>
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
                          type='button'
                          className='btn btn-secondary'
                          onClick={() => this.handleModal()}
                        >
                          Close Modal
                        </button>
                      </Modal.Footer>
                    </Modal>
                  </div>
                  <button
                    type='button'
                    className='btn btn-secondary'
                    style={{ height: 40, marginTop: 30 }}
                    onClick={() => this.handleModal()}
                  >
                    Add Status
                  </button>
                </div>
                <div className='form-row'>
                  <div className='form-group col-md'>
                    <label htmlFor='resourceQuality'>Resource Quality</label>
                    <ReactStars
                      count={5}
                      onChange={this.ratingChanged}
                      size={24}
                      half={false}
                      color2={"#ffd700"}
                      value={this.state.resourceQuality}
                      required
                    />
                  </div>
                </div>
              </div>
            </div>
            <button
              className='btn btn-primary'
              type='button'
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
