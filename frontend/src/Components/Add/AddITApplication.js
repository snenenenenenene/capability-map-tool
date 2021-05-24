import React, { Component } from "react";
import { Link } from "react-router-dom";
import axios from "axios";
import toast from "react-hot-toast";

export default class AddITApplication extends Component {
  constructor(props) {
    super(props);
    this.state = {
      environments: [],

      modalIsOpen: false,
      setIsOpen: false,

      environmentName: this.props.match.params.name,
      environmentId: "",
      itApplicationName: "",
      technology: "",
      version: "",
      costCurrency: "",
      currentTotalCostPerYear: "",
    };
    this.handleSubmit = this.handleSubmit.bind(this);
    this.handleInputChange = this.handleInputChange.bind(this);
  }

  handleSubmit = async (e) => {
    e.preventDefault();
    const formData = new FormData();
    formData.append("itApplicationName", this.state.itApplicationName);
    formData.append("technology", this.state.technology);
    formData.append("version", this.state.version);
    formData.append("costCurrency", this.state.costCurrency);
    formData.append("currentTotalCostPerYear", this.state.currentTotalCostPerYear);
    await axios
      .post(`${process.env.REACT_APP_API_URL}/itapplication/`, formData)
      .then((response) => toast.success("IT Application Added Successfully!"))
      .catch((error) => toast.error("Could not Add IT Application"));
    this.props.history.push(
      `/environment/${this.state.environmentName}/itapplication`
    );
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
        console.log(error);
        this.props.history.push("/notfounderror");
      });

    await axios
      .get(
        `${process.env.REACT_APP_API_URL}/itapplication/`
      )
      .then((response) => {
        this.setState({ capabilities: response.data });
      })
      .catch((error) => {
        console.log(error);
        toast.error("Could Not Find IT Applications");
      });
  }

  handleInputChange(event) {
    this.setState({ [event.target.name]: event.target.value });
  }

  openModal() {
    this.setState({ setIsOpen: true });
  }

  closeModal() {
    this.setState({ setIsOpen: false });
  }

  render() {
    const environmentName = this.props.match.params.name;

    return (
      <div>
        <br></br>
        <nav aria-label='shadow breadcrumb'>
          <ol className='breadcrumb'>
            <li className='breadcrumb-item'>
              <Link to={`/`}>Home</Link>
            </li>
            <li className='breadcrumb-item'>
              <Link to={`/environment/${environmentName}`}>
                {environmentName}
              </Link>
            </li>
            <li className='breadcrumb-item active' aria-current='page'>
              Add IT Application
            </li>
          </ol>
        </nav>
        <div className='jumbotron shadow'>
          <h3>Add IT Application</h3>
          <form onSubmit={this.handleSubmit}>
            <div className='row'>
              <div className='col-sm'>
                <div className='form-row'>
                  <div className='form-group col-md'>
                    <label htmlFor='itApplicationName'>
                      Name IT-Application
                    </label>
                    <input
                      type='text'
                      id='itApplicationName'
                      name='itApplicationName'
                      className='form-control'
                      placeholder='Name IT-Application'
                      value={this.state.itApplicationName}
                      onChange={this.handleInputChange}
                    />
                  </div>
                </div>
                <div className='form-row'>
                  <div className='form-group col-md'>
                    <label htmlFor='technology'>Technology</label>
                    <input
                      type='text'
                      id='technology'
                      name='technology'
                      className='form-control'
                      placeholder='Technology'
                      value={this.state.technology}
                      onChange={this.handleInputChange}
                    />
                  </div>
                </div>
                <div className='form-row'>
                  <div className='form-group col-md'>
                    <label htmlFor='version'>Version</label>
                    <input
                      type='text'
                      id='version'
                      name='version'
                      className='form-control'
                      placeholder='Version'
                      value={this.state.version}
                      onChange={this.handleInputChange}
                    />
                  </div>
                </div>
              </div>
              <div className='col-sm'>
                <div className='form-row'>
                  <div className='form-group col-md'>
                    <label htmlFor='costCurrency'>Cost Currency</label>
                    <input
                      type='text'
                      id='costCurrency'
                      name='costCurrency'
                      className='form-control'
                      placeholder='Cost Currency'
                      value={this.state.costCurrency}
                      onChange={this.handleInputChange}
                    />
                  </div>
                </div>
                <div className='form-row'>
                  <div className='form-group col-md'>
                    <label htmlFor='currentTotalCostPerYear'>
                      Current Total Cost Per Year
                    </label>
                    <input
                      type='text'
                      id='currentTotalCostPerYear'
                      name='currentTotalCostPerYear'
                      className='form-control'
                      placeholder='Current Total Cost Per Year'
                      value={this.state.currentTotalCostPerYear}
                      onChange={this.handleInputChange}
                    />
                  </div>
                </div>
                <div className='form-row'>
                  <div className='form-group col-md'>
                    <label htmlFor='toleratedTotalCostPerYear'>
                      Tolerated Total Cost Per Year
                    </label>
                    <input
                      type='text'
                      id='toleratedTotalCostPerYear'
                      name='toleratedTotalCostPerYear'
                      className='form-control'
                      placeholder='Tolerated Total Cost Per Year'
                      value={this.state.toleratedTotalCostPerYear}
                      onChange={this.handleInputChange}
                    />
                  </div>
                </div>
              </div>
              <div className='col-sm'>
                <div className='form-row'>
                  <div className='form-group col-md'>
                    <label htmlFor='itApplicationName'>
                      Name IT-Application
                    </label>
                    <input
                      type='date'
                      id='itApplicationName'
                      name='itApplicationName'
                      className='form-control'
                      placeholder='Name IT-Application'
                      value={this.state.itApplicationName}
                      onChange={this.handleInputChange}
                    />
                  </div>
                </div>
                <div className='form-row'>
                  <div className='form-group col-md'>
                    <label htmlFor='technology'>Technology</label>
                    <input
                      type='date'
                      id='technology'
                      name='technology'
                      className='form-control'
                      placeholder='Technology'
                      value={this.state.technology}
                      onChange={this.handleInputChange}
                    />
                  </div>
                </div>
                <div className='form-row'>
                  <div className='form-group col-md'>
                    <label htmlFor='version'>Version</label>
                    <input
                      type='time'
                      id='version'
                      name='version'
                      className='form-control'
                      placeholder='Version'
                      value={this.state.version}
                      onChange={this.handleInputChange}
                    />
                  </div>
                </div>
              </div>
            </div>
            <button
              className='btn btn-secondary'
              type='button'
              onClick={this.handleSubmit}
            >
              Submit
            </button>
            <button
              className='btn btn-primary float-right'
              type='button'
              data-toggle='modal'
              data-target='#exampleModal'
              data-whatever='@getbootstrap'
            >
              Ratings
            </button>

            {/*<div>*/}
            {/*    <button onClick={this.openModal}>Open Modal</button>*/}
            {/*    <Modal*/}
            {/*        isOpen={this.state.modalIsOpen}*/}
            {/*        onAfterOpen={this.afterOpenModal}*/}
            {/*        onRequestClose={this.closeModal}*/}
            {/*        contentLabel="Example Modal"*/}
            {/*    >*/}

            {/*        <h2>Hello</h2>*/}
            {/*        <button onClick={this.state.closeModal}>close</button>*/}
            {/*        <div>I am a modal</div>*/}
            {/*        <form>*/}
            {/*            <input />*/}
            {/*            <button>tab navigation</button>*/}
            {/*            <button>stays</button>*/}
            {/*            <button>inside</button>*/}
            {/*            <button>the modal</button>*/}
            {/*        </form>*/}
            {/*    </Modal>*/}
            {/*</div>*/}
          </form>
        </div>
      </div>
    );
  }
}
