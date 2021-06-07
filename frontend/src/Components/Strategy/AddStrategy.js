import React, { Component } from "react";
import { Link } from "react-router-dom";
import toast from "react-hot-toast";
import { Modal } from "react-bootstrap";
import Select from "react-select";
import StatusQuickAdd from "../Status/StatusQuickAdd";
import API from "../../Services/API";

export default class AddStrategy extends Component {
  constructor(props) {
    super(props);
    this.state = {
      api: new API(),

      environments: [],
      statuses: [],
      environmentName: this.props.match.params.name,
      environmentId: "",
      strategyName: "",
      statusId: "",
      timeFrameStart: new Date(),
      timeFrameEnd: new Date(),
    };
    this.handleSubmit = this.handleSubmit.bind(this);
    this.handleInputChange = this.handleInputChange.bind(this);
    this.handleDateChange = this.handleDateChange.bind(this);
    this.updateDate = this.updateDate.bind(this);
  }

  handleSubmit = async (e) => {
    e.preventDefault();
    const formData = new FormData();
    formData.append("environmentId", this.state.environmentId);
    formData.append("strategyName", this.state.strategyName);
    formData.append("timeFrameStart", this.state.timeFrameStart);
    formData.append("timeFrameEnd", this.state.timeFrameEnd);
    formData.append("statusId", this.state.statusId);
    await this.state.api.endpoints.strategy
      .create(formData)
      .then((response) => {
        toast.success("Strategy Added Successfully!");
        this.props.history.push(
          `/environment/${this.state.environmentName}/strategy`
        );
      })
      .catch((error) => toast.error("Could not Add Strategy"));
  };

  async componentDidMount() {
    this.state.api.createEntity({ name: "environment" });
    this.state.api.createEntity({ name: "status" });
    this.state.api.createEntity({ name: "strategy" });

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
      .then((response) => {
        response.data.forEach((status) => {
          status.label = status.validityPeriod;
          status.value = status.statusId;
        });
        this.setState({ statuses: response.data });
      })
      .catch((error) => {
        toast.error("Could not load Statuses");
      });
  }

  handleStatusModal() {
    this.setState({ showStatusModal: !this.state.showStatusModal });
  }

  async updateDate() {
    await this.state.api.endpoints.status
      .getAll()
      .then((response) => {
        response.data.forEach((status) => {
          status.label = status.validityPeriod;
          status.value = status.statusId;
        });
        this.setState({ statuses: response.data });
      })
      .catch((error) => {
        toast.error("Could not Update Statuses");
      });
  }

  handleDateChange(event) {
    this.setState({ [event.target.name]: event.target.value.toLocaleString() });
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
              <Link to={`/environment/${this.state.environmentName}/strategy`}>
                Strategy
              </Link>
            </li>
            <li className='breadcrumb-item active' aria-current='page'>
              Add Strategy
            </li>
          </ol>
        </nav>
        <div className='jumbotron'>
          <h3>Add Strategy</h3>
          <form onSubmit={this.handleSubmit}>
            <div className='row'>
              <div className='col-sm-6'>
                <div className='form-row'>
                  <div className='form-group col-md'>
                    <label htmlFor='nameCapability'>Name Strategy</label>
                    <input
                      type='text'
                      id='strategyName'
                      name='strategyName'
                      className='form-control'
                      placeholder='Name Strategy'
                      value={this.state.strategyName}
                      onChange={this.handleInputChange}
                    />
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
                      onChange={(status) => {
                        this.setState({ statusId: status.statusId });
                      }}
                    ></Select>
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
                          type='button'
                          className='btn btn-secondary'
                          onClick={() => this.handleStatusModal()}
                        >
                          Close
                        </button>
                      </Modal.Footer>
                    </Modal>
                  </div>
                  <button
                    type='button'
                    className='btn btn-secondary'
                    style={{ height: 40, marginTop: 30 }}
                    onClick={() => this.handleStatusModal()}
                  >
                    Add Status
                  </button>
                </div>
              </div>
              <div className='col-sm-6'>
                <div className='form-row'>
                  <div className='form-group col-md-6'>
                    <label htmlFor='timeFrameStart'>Time Frame Start</label>
                    <input
                      type='date'
                      id='timeFrameStart'
                      name='timeFrameStart'
                      className='form-control'
                      placeholder='Start Date'
                      value={this.state.timeFrameStart}
                      onChange={this.handleDateChange}
                    />
                  </div>
                </div>
                <div className='form-row'>
                  <div className='form-group col-md-6'>
                    <label htmlFor='timeFrameEnd'>Time Frame End</label>
                    <input
                      type='date'
                      id='timeFrameEnd'
                      name='timeFrameEnd'
                      className='form-control'
                      placeholder='End Date'
                      value={this.state.timeFrameEnd}
                      onChange={this.handleDateChange}
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
