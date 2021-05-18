import React, {Component} from 'react';
import {Link} from 'react-router-dom';
import axios from 'axios';
import toast, { Toaster } from 'react-hot-toast';

export default class AddStrategy extends Component {
    constructor(props) {
        super(props);
        this.state = {
            environments: [],
            environmentName: this.props.match.params.name,
            environmentId:'',
            strategyName: '',
            timeFrameStart: new Date(),
            timeFrameEnd: new Date(),
        };
        this.handleSubmit = this.handleSubmit.bind(this);
        this.handleInputChange = this.handleInputChange.bind(this)
        this.handleDateChange = this.handleDateChange.bind(this)
    }

    handleSubmit = async e => {
        e.preventDefault();
        const formData = new FormData()
        formData.append('environmentId', this.state.environmentId)
        formData.append('strategyName', this.state.strategyName)
        formData.append('timeFrameStart', this.state.timeFrameStart)
        formData.append('timeFrameEnd', this.state.timeFrameEnd)
        console.log(this.state.environmentId)
        console.log(this.state.strategyName)
        console.log(this.state.timeFrameStart)
        console.log(this.state.timeFrameEnd)
        await axios.post(`${process.env.REACT_APP_API_URL}/strategy/`, formData)
        .then(response => toast.success("Strategy Added Successfully!"))
        .catch(error => toast.error("Could not Add Strategy"))
        this.props.history.push(`/environment/${this.state.environmentName}/strategy`)
    }

    async componentDidMount() {
        await axios.get(`${process.env.REACT_APP_API_URL}/environment/environmentname/${this.state.environmentName}`)
        .then(response => {
            this.setState({environmentId: response.data.environmentId})
        })
        .catch(error => {
            console.log(error)
            this.props.history.push('/notfounderror')
        })
    }

    handleDateChange(event) {
        this.setState({[event.target.name] : event.target.value.toLocaleString()})
    }


    handleInputChange(event) {
        this.setState({ [event.target.name]: event.target.value })
    }

    render() {
        return (
            <div>
                <nav aria-label="breadcrumb">
                    <ol className="breadcrumb">
                        <li className="breadcrumb-item"><Link to={`/`}>Home</Link></li>
                        <li className="breadcrumb-item"><Link to={`/environment/${this.state.environmentName}`}>{this.state.environmentName}</Link></li>
                        <li className="breadcrumb-item"><Link to={`/environment/${this.state.environmentName}/strategy`}>Strategy</Link></li>
                        <li className="breadcrumb-item active" aria-current="page">Add Strategy</li>
                    </ol>
                </nav>
            <div className="jumbotron">
                <h3>Add Strategy</h3>
                <form onSubmit={this.handleSubmit}>
                    <div className="row">
                        <div className="col-sm-6">
                            <div className="form-row">
                                <div className="form-group col-md">
                                    <label htmlFor="nameCapability">Name Strategy</label>
                                <input type="text" id="strategyName" name="strategyName" className="form-control" placeholder="Name Strategy"
                                       value={this.state.strategyName} onChange={this.handleInputChange}/>
                                </div>
                            </div>
                        </div>
                        <div className="col-sm-6">
                        <div className="form-row">
                                    <div className="form-group col-md-6">
                                        <label htmlFor="timeFrameStart">Time Frame Start</label>
                                        <input type="date" id="timeFrameStart" name="timeFrameStart" className="form-control" placeholder="Start Date"
                                               value={this.state.timeFrameStart} onChange={this.handleDateChange}/>
                                    </div>
                                </div>
                                <div className="form-row">
                                    <div className="form-group col-md-6">
                                        <label htmlFor="timeFrameEnd">Time Frame End</label>
                                        <input type="date" id="timeFrameEnd" name="timeFrameEnd" className="form-control" placeholder="End Date"
                                               value={this.state.timeFrameEnd} onChange={this.handleDateChange}/>
                                    </div>
                                </div>
                    </div>
                    </div>
                    <button className="btn btn-primary" type="button" onClick={this.handleSubmit}>Submit</button>
                </form>
            </div>
        </div>
        )
    }
}