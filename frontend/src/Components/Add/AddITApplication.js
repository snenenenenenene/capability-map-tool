import React, {Component} from 'react';
import {Link} from 'react-router-dom';
import Modal from 'react-modal';

export default class AddITApplication extends Component {
    constructor(props) {
        super(props);
        this.state = {
            statuses: [],
            environments: [],
            capabilities: [],

            modalIsOpen: false,
            setIsOpen: false,

            environmentName: this.props.match.params.name,
            environmentId:'',
            itApplicationName: '',
            technology: '',
            version: '',
            costCurrency: '',
            currentTotalCostPerYear: ''
        };
        this.handleSubmit = this.handleSubmit.bind(this);
        this.handleInputChange = this.handleInputChange.bind(this)
        this.openModal = this.openModal.bind(this)
        this.afterOpenModal = this.afterOpenModal.bind(this)
    }

    handleSubmit = async e => {
        e.preventDefault();
        const formData = new FormData()
        formData.append('environmentName', this.state.environmentName)
        formData.append('environmentId', this.state.environmentId)
        formData.append('capabilityName', this.state.capabilityName)
        formData.append('parentCapabilityId', 1)
        // formData.append('parentCapabilityId', this.state.parentCapability)
        formData.append('paceOfChange', this.state.paceOfChange)
        // formData.append('targetOperatingModel', this.state.TOM)
        formData.append('targetOperatingModel', "soepke")
        formData.append('informationQuality', this.state.informationQuality)
        formData.append('applicationFit', this.state.applicationFit)
        formData.append('resourceQuality', this.state.resourcesQuality)
        formData.append('statusId', this.state.statusId)
        formData.append('level', this.state.capabilityLevel)
        for(let [name, value] of formData) {
            console.log(`${name} = ${value}`);
        }
        await fetch(`http://localhost:8080/capability/add`,{
            method: "POST",
            body: formData
        }).then(function (res) {
            if (res.ok) {
                console.log("Capability added");
            } else if (res.status === 401) {
                console.log("Oops,, Something went wrong");
            }})

    }

    async componentDidMount() {
        const environmentResponse = await fetch(`http://localhost:8080/environment/environmentname/${this.state.environmentName}`);
        const environmentData = await environmentResponse.json();
        this.setState({environmentId: environmentData.environmentId});

        const statusResponse = await fetch(`http://localhost:8080/status/all`)
        const statusData = await statusResponse.json();
        this.setState({statuses: statusData});

        const capabilityResponse = await fetch(`http://localhost:8080/api/capability/all`);
        const capabilityData = await capabilityResponse.json();
        this.setState({capabilities: capabilityData});
    }

    handleInputChange(event) {
        this.setState({ [event.target.name]: event.target.value })
    }

    statusListRows() {
        return this.state.statuses.map((status) => {
            return <option key={status.statusId} value={status.statusId}>{status.validityPeriod}</option>
        })
    }


    capabilityListRows() {
        return this.state.capabilities.map((capability) => {
            return <option key={capability.capabilityId} value={capability.capabilityId}>{capability.capabilityName}</option>
        })
    }

    openModal() {
        this.setState({setIsOpen: true});
    }

    closeModal(){
        this.setState({setIsOpen: false});
    }



    render() {
        const environmentName = this.props.match.params.name;

        return (
            <div>
                <nav aria-label="breadcrumb">
                    <ol className="breadcrumb">
                        <li className="breadcrumb-item"><Link to={`/`}>Home</Link></li>
                        <li className="breadcrumb-item"><Link to={`/environment/${environmentName}`}>{environmentName}</Link></li>
                        <li className="breadcrumb-item active" aria-current="page">Add IT Application</li>
                    </ol>
                </nav>
                <div className="jumbotron">
                    <h3>Add IT Application</h3>
                    <form onSubmit={this.handleSubmit}>
                        <div className="row">
                            <div className="col-sm">
                                <div className="form-row">
                                    <div className="form-group col-md">
                                        <label htmlFor="itApplicationName">Name IT-Application</label>
                                        <input type="text" id="itApplicationName" name="itApplicationName" className="form-control" placeholder="Name IT-Application"
                                               value={this.state.itApplicationName} onChange={this.handleInputChange}/>
                                    </div>
                                </div>
                                <div className="form-row">
                                    <div className="form-group col-md">
                                        <label htmlFor="technology">Technology</label>
                                        <input type="text" id="technology" name="technology" className="form-control" placeholder="Technology"
                                               value={this.state.technology} onChange={this.handleInputChange}/>
                                    </div>
                                </div>
                                <div className="form-row">
                                    <div className="form-group col-md">
                                        <label htmlFor="version">Version</label>
                                        <input type="text" id="version" name="version" className="form-control" placeholder="Version"
                                               value={this.state.version} onChange={this.handleInputChange}/>
                                    </div>
                                </div>
                            </div>
                            <div className="col-sm">
                                <div className="form-row">
                                    <div className="form-group col-md">
                                        <label htmlFor="costCurrency">Cost Currency</label>
                                        <input type="text" id="costCurrency" name="costCurrency" className="form-control" placeholder="Cost Currency"
                                               value={this.state.costCurrency} onChange={this.handleInputChange}/>
                                    </div>
                                </div>
                                <div className="form-row">
                                    <div className="form-group col-md">
                                        <label htmlFor="currentTotalCostPerYear">Current Total Cost Per Year</label>
                                        <input type="text" id="currentTotalCostPerYear" name="currentTotalCostPerYear" className="form-control" placeholder="Current Total Cost Per Year"
                                               value={this.state.currentTotalCostPerYear} onChange={this.handleInputChange}/>
                                    </div>
                                </div>
                                <div className="form-row">
                                    <div className="form-group col-md">
                                        <label htmlFor="toleratedTotalCostPerYear">Tolerated Total Cost Per Year</label>
                                        <input type="text" id="toleratedTotalCostPerYear" name="toleratedTotalCostPerYear" className="form-control" placeholder="Tolerated Total Cost Per Year"
                                               value={this.state.toleratedTotalCostPerYear} onChange={this.handleInputChange}/>
                                    </div>
                                </div>
                            </div>
                            <div className="col-sm">
                                <div className="form-row">
                                    <div className="form-group col-md">
                                        <label htmlFor="itApplicationName">Name IT-Application</label>
                                        <input type="date" id="itApplicationName" name="itApplicationName" className="form-control" placeholder="Name IT-Application"
                                               value={this.state.itApplicationName} onChange={this.handleInputChange}/>
                                    </div>
                                </div>
                                <div className="form-row">
                                    <div className="form-group col-md">
                                        <label htmlFor="technology">Technology</label>
                                        <input type="date" id="technology" name="technology" className="form-control" placeholder="Technology"
                                               value={this.state.technology} onChange={this.handleInputChange}/>
                                    </div>
                                </div>
                                <div className="form-row">
                                    <div className="form-group col-md">
                                        <label htmlFor="version">Version</label>
                                        <input type="time" id="version" name="version" className="form-control" placeholder="Version"
                                               value={this.state.version} onChange={this.handleInputChange}/>
                                    </div>
                                </div>
                            </div>

                        </div>
                        <button className="btn btn-secondary" type="button" onClick={this.handleSubmit}>Submit</button>
                        <button className="btn btn-primary float-right" type="button" data-toggle="modal" data-target="#exampleModal" data-whatever="@getbootstrap">Ratings</button>

                        <div>
                            <button onClick={this.openModal}>Open Modal</button>
                            <Modal
                                isOpen={this.state.modalIsOpen}
                                onAfterOpen={this.afterOpenModal}
                                onRequestClose={this.closeModal}
                                contentLabel="Example Modal"
                            >

                                <h2>Hello</h2>
                                <button onClick={this.state.closeModal}>close</button>
                                <div>I am a modal</div>
                                <form>
                                    <input />
                                    <button>tab navigation</button>
                                    <button>stays</button>
                                    <button>inside</button>
                                    <button>the modal</button>
                                </form>
                            </Modal>
                        </div>

                    </form>
                </div>
            </div>
        )
    }
}